## Context

The app is a single-Activity Kotlin Android app (`GrowthActivity`) with one layout (`activity_growth.xml`). It uses `SharedPreferences` (`growth_prefs`) to persist growth value, exponent, and rate index. The current theme is `Theme.BasicApp` (purple Material Components), hardcoded in the manifest via `android:theme="@style/Theme.BasicApp"`.

Six art-pass theme documents exist in `hidden/THEME_*.md`, each defining colors, a theme style, layout modifications, drawables, and optional runtime Kotlin effects. The app has no theme-switching mechanism and no settings UI.

The app uses `ConstraintLayout` with two main sections: a `growthNumber` `TextView` spanning most of the screen, and a `rateControls` `LinearLayout` pinned to the bottom containing Slower/Faster buttons and a rate label.

MinSdk is 24, targetSdk is 34. Dependencies: core-ktx, appcompat, material, constraintlayout. No view binding or data binding is enabled.

## Goals / Non-Goals

**Goals:**

- Add a `Spinner` dropdown to `activity_growth.xml` that lets the user select a theme at runtime.
- Apply the selected theme immediately via `setTheme()` + `recreate()`, persisting the choice in `SharedPreferences`.
- Deliver in two phases: Phase 1 (dropdown + Minimalist Noir only), Phase 2 (remaining five themes).
- Phase 1 must be fully verifiable by a human on-device before Phase 2 begins.
- Each theme style must be a full Material Components theme overriding window background, status bar, navigation bar, text color, and all color attributes.

**Non-Goals:**

- Day/night auto-switching based on system setting.
- Crossfade or animation between theme changes — `recreate()` flash is acceptable.
- Theme preview before applying — themes apply immediately on selection.
- Per-theme font families (except monospace for Terminal, which is a Phase 2 concern).
- Custom theme editor or color picker.
- Network theme downloading or sharing.

## Decisions

### D1: Theme application strategy — `setTheme()` before `setContentView()` + `recreate()`

**Decision:** Apply the theme in `onCreate()` by calling `setTheme(resId)` before `super.onCreate()` and `setContentView()`. When the user picks a new theme from the spinner, save the preference and call `recreate()`.

**Rationale:** This is the standard Android mechanism for runtime theme switching. `setTheme()` must be called before the activity's view hierarchy is created. `recreate()` destroys and re-creates the activity, which is the only reliable way to fully apply a new theme to all views including system bars.

**Alternative considered:** Dynamically updating view colors without recreation. Rejected because: (a) the THEME_*.md files define full theme styles including `android:windowBackground`, `android:statusBarColor`, and `android:navigationBarColor` which cannot be changed on existing windows; (b) it would require per-view manual color assignment instead of leveraging the theme system; (c) it creates inconsistency between themed and manually-colored elements.

### D2: Theme identification — string keys mapped to style resource IDs

**Decision:** Define an enum-like structure that maps human-readable theme keys (stored in SharedPreferences) to Android style resource IDs.

```
Theme keys (stored in SharedPreferences):
  "default"            → R.style.Theme_BasicApp
  "minimalist_noir"    → R.style.Theme_BasicApp_Noir
  "terminal_phosphor"  → R.style.Theme_BasicApp_Terminal
  "abyssal_ocean"      → R.style.Theme_BasicApp_Ocean
  "solar_flare"        → R.style.Theme_BasicApp_Solar
  "cosmic_nebula"      → R.style.Theme_BasicApp_Nebula
  "emerald_growth"     → R.style.Theme_BasicApp_Emerald
```

The SharedPreferences value for `KEY_THEME` is a `String` (one of the keys above). A helper function resolves the key to a style resource ID. Unknown or missing keys resolve to `R.style.Theme_BasicApp` (Default).

**Rationale:** String keys are human-readable in SharedPreferences, survive app updates, and allow stub entries in Phase 1 (the five unimplemented themes resolve to Default until Phase 2 adds their styles).

**Alternative considered:** Storing the style resource ID integer directly. Rejected because: (a) resource IDs change between builds with `aapt` remapping; (b) not human-readable in the prefs file; (c) harder to handle forward-compatibility.

### D3: Spinner placement — above rateControls, full width

**Decision:** Add the `Spinner` between `growthNumber` and `rateControls` in the ConstraintLayout. The spinner sits in a horizontal band at the bottom of the screen, above the rate controls, constrained to the bottom of `growthNumber` and the top of `rateControls`. The `Spinner` uses `android:backgroundTint` and `android:popupBackground` styled by the current theme so it visually matches.

Layout constraint chain:
```
growthNumber (top → parent top, bottom → themeSpinner top)
themeSpinner (top → growthNumber bottom, bottom → rateControls top)
rateControls (top → themeSpinner bottom, bottom → parent bottom)
```

**Rationale:** Placing the spinner above the rate controls keeps it accessible without obscuring the growth number. It is visually grouped with the controls area. The spinner takes minimal vertical space and does not interfere with the auto-sizing `TextView`.

**Alternative considered:** A toolbar or menu dropdown. Rejected because: (a) the action bar is hidden (`supportActionBar?.hide()`); (b) adding a toolbar changes the entire screen layout; (c) a bottom-anchored spinner is more discoverable for this simple single-screen app.

### D4: Spinner items — string array resource

**Decision:** Define the theme display names as a string array resource (`R.array.theme_names`) paired with a matching string array of theme keys (`R.array.theme_keys`). The `Spinner` adapter reads `theme_names` for display. When a selection is made, the corresponding `theme_keys` entry provides the preference key to save.

```xml
<string-array name="theme_names">
    <item>Default</item>
    <item>Minimalist Noir</item>
    <item>Terminal / Phosphor</item>
    <item>Abyssal Ocean</item>
    <item>Solar Flare</item>
    <item>Cosmic Nebula</item>
    <item>Emerald Growth</item>
</string-array>
<string-array name="theme_keys">
    <item>default</item>
    <item>minimalist_noir</item>
    <item>terminal_phosphor</item>
    <item>abyssal_ocean</item>
    <item>solar_flare</item>
    <item>cosmic_nebula</item>
    <item>emerald_growth</item>
</string-array>
```

Both arrays must have the same length and same order. The position in the spinner maps to the same position in `theme_keys`.

**Rationale:** String arrays are easy to maintain, support localization if needed later, and avoid hardcoding theme metadata in Kotlin.

### D5: Theme style naming convention

**Decision:** Each theme style follows the pattern `Theme.BasicApp.<Suffix>` where the suffix matches the theme's character:

| Key                | Style Name                  |
|--------------------|-----------------------------|
| `default`          | `Theme.BasicApp`            |
| `minimalist_noir`  | `Theme.BasicApp.Noir`       |
| `terminal_phosphor`| `Theme.BasicApp.Terminal`   |
| `abyssal_ocean`    | `Theme.BasicApp.Ocean`      |
| `solar_flare`      | `Theme.BasicApp.Solar`      |
| `cosmic_nebula`    | `Theme.BasicApp.Nebula`     |
| `emerald_growth`   | `Theme.BasicApp.Emerald`    |

All art-pass themes inherit from `Theme.MaterialComponents.DayNight.NoActionBar` (not `DarkActionBar`) because the app hides the action bar anyway, and NoActionBar gives full control over the chrome.

**Rationale:** Consistent naming avoids collisions. The Default theme retains its current parent (`DarkActionBar`) since it preserves the existing app appearance. All art-pass themes use `NoActionBar` since they are dark immersive themes.

### D6: Phase 1 fallback behavior — unimplemented themes resolve to Default

**Decision:** In Phase 1, the `themeKeyToStyleRes` mapping returns `R.style.Theme_BasicApp` for the five unimplemented theme keys. The spinner shows all seven entries, but only "Default" and "Minimalist Noir" produce visual changes. Selecting any other entry saves that key to preferences and recreates, but the resulting appearance is identical to Default.

**Rationale:** This avoids runtime crashes (no missing resource IDs), keeps the dropdown fully populated for visual QA, and makes Phase 2 simply a matter of adding new style entries and color resources. The user sees all options and can confirm the fallback works.

### D7: Minimalist Noir theme — color resources only, no runtime effects

**Decision:** The Phase 1 Noir implementation uses only theme style attributes and color/drawable resources. No Kotlin runtime effects (accent flash, fade-in) are included in Phase 1. The Noir theme style sets `windowBackground`, `statusBarColor`, `navigationBarColor`, `textColor`, and all Material color attributes. The only drawable is `hairline_top.xml` for the separator above controls.

**Rationale:** Minimalist Noir is explicitly chosen as the Phase 1 theme because its design document describes no particle effects, no gradient backgrounds, no pulsing glow, and no animations. This makes it the cleanest test case for verifying that the theme-switching mechanism itself works before adding runtime complexity.

### D8: SharedPreferences key for theme

**Decision:** Add `KEY_THEME = "selected_theme"` to the existing `growth_prefs` file, with default value `"default"`. Read it in `loadState()`, save it in `saveState()` and also immediately on spinner selection (before `recreate()`).

**Rationale:** Reusing the existing prefs file avoids creating a second file. The key name is descriptive and won't collide with existing keys.

### D9: Activity recreation preserves runtime state

**Decision:** When `recreate()` is called after a theme change, the existing `SharedPreferences` persistence handles state restoration. The growth value, exponent, and rate index are already saved and restored in `loadState()`/`saveState()`. The `onPause()` save plus the immediate save before `recreate()` ensures no data loss.

**Rationale:** `recreate()` destroys and re-creates the Activity, but `onPause()` fires first (triggering `saveState()`). The new Activity instance calls `loadState()` in `onCreate()`. This is the existing lifecycle pattern and needs no changes for state preservation during theme switches.

### D10: Spinner styling — themed to match current theme

**Decision:** The `Spinner` uses `style="@style/Widget.MaterialComponents.Spinner.Underlined"` and sets `android:backgroundTint` via the current theme's `colorPrimary`. The dropdown popup background uses `android:popupBackground` set to the theme's surface color. This means the spinner visually adapts to whichever theme is active.

**Rationale:** Material Components Spinner with Underlined style provides a consistent, minimal dropdown that fits the app's simple aesthetic. The themed tint ensures it looks correct in all color schemes without manual per-theme overrides.

## Risks / Trade-offs

**[Recreate flash]** → `recreate()` causes a visible flash as the Activity is destroyed and re-created. Mitigation: this is acceptable per the non-goals. A future change could add a crossfade, but it is explicitly out of scope.

**[Spinner takes vertical space from growth number]** → The growth number `TextView` uses auto-sizing (`autoSizeTextType="uniform"` with 12sp–72sp range). The spinner adds roughly 48dp of vertical space at the bottom, reducing the growth number's available height. Mitigation: auto-sizing handles this gracefully — the number simply uses a slightly smaller max size. On very small screens, the minimum 12sp ensures readability.

**[Phase 1 user confusion from fallback themes]** → Selecting an unimplemented theme in Phase 1 produces the Default appearance, which may confuse a tester who expects a visual change. Mitigation: the human handoff verification explicitly checks this case. Phase 1 is not a release — it is a verification gate.

**[Style resource ID mapping must be updated in Phase 2]** → When Phase 2 adds the five remaining themes, the Kotlin `themeKeyToStyleRes` function must be updated to return the new style IDs. Mitigation: this is a single function with a `when` block — straightforward and low-risk. The Phase 2 tasks will explicitly include updating this mapping.

## Migration Plan

No migration is needed. The `selected_theme` preference defaults to `"default"`, which resolves to the existing `Theme.BasicApp`. First-launch behavior is identical to the current app. The manifest `android:theme` attribute remains `Theme.BasicApp` as the fallback before `setTheme()` runs.

## Open Questions

None. All design decisions are resolved.