## 1. Phase 1 — Theme-switching mechanism and Minimalist Noir

- [x] 1.1 Add Minimalist Noir color resources to `colors.xml`. Add all 10 Noir colors: `true_black` (#FF000000), `carbon` (#FF121212), `carbon_light` (#FF1E1E1E), `pure_white` (#FFFFFFFF), `silver` (#FFB0B0B0), `ghost_gray` (#FF666666), `signal_red` (#FFFF1744), `signal_dim` (#FFB71C1C), `hairline` (#FF1C1C1C), `mid_gray` (#FF888888).
  Done when: `colors.xml` contains all 10 Noir color entries and `./gradlew assembleDebug` succeeds.

- [x] 1.2 Add `Theme.BasicApp.Noir` style to `themes.xml` inheriting from `Theme.MaterialComponents.DayNight.NoActionBar` with all attributes per the Minimalist Noir spec: `colorPrimary` → `pure_white`, `colorPrimaryVariant` → `silver`, `colorOnPrimary` → `true_black`, `colorSecondary` → `signal_red`, `colorSecondaryVariant` → `signal_dim`, `colorOnSecondary` → `pure_white`, `android:windowBackground` → `true_black`, `android:statusBarColor` → `true_black`, `android:navigationBarColor` → `true_black`, `android:textColor` → `pure_white`.
  Done when: `themes.xml` contains `Theme.BasicApp.Noir` with all 10 attribute mappings and `./gradlew assembleDebug` succeeds.

- [x] 1.3 Create `res/drawable/hairline_top.xml` — a layer-list drawable with a 1dp top border in `@color/hairline`, per the Minimalist Noir THEME document.
  Done when: `res/drawable/hairline_top.xml` exists, references `@color/hairline`, and `./gradlew assembleDebug` succeeds.

- [x] 1.4 Add string array resources to `strings.xml` (or a new `arrays.xml`): `theme_names` with 7 items (Default, Minimalist Noir, Terminal / Phosphor, Abyssal Ocean, Solar Flare, Cosmic Nebula, Emerald Growth) and `theme_keys` with 7 matching items (default, minimalist_noir, terminal_phosphor, abyssal_ocean, solar_flare, cosmic_nebula, emerald_growth). Both arrays MUST have the same length and same order.
  Done when: `R.array.theme_names` and `R.array.theme_keys` both resolve, have length 7, and items at each index correspond; `./gradlew assembleDebug` succeeds.

- [x] 1.5 Add `themeSpinner` Spinner to `activity_growth.xml` between `growthNumber` and `rateControls`. Update the ConstraintLayout chain so `growthNumber` bottom constrains to `themeSpinner` top, `themeSpinner` top/bottom constrains between `growthNumber` bottom and `rateControls` top, and `themeSpinner` spans full width with `0dp` width constrained to parent start/end. Use `style="@style/Widget.MaterialComponents.Spinner.Underlined"` and `android:backgroundTint="?attr/colorPrimary"`.
  Done when: `activity_growth.xml` contains a Spinner with id `themeSpinner`, positioned between `growthNumber` and `rateControls` in the constraint chain, and `./gradlew assembleDebug` succeeds.

- [x] 1.6 Add theme infrastructure to `GrowthActivity.kt`: (a) add `KEY_THEME = "selected_theme"` companion constant; (b) add `private var selectedTheme = "default"` field; (c) add `themeKeyToStyleRes(key: String): Int` function that maps `"default"` → `R.style.Theme_BasicApp`, `"minimalist_noir"` → `R.style.Theme_BasicApp_Noir`, and all other keys → `R.style.Theme_BasicApp` (Phase 1 fallback); (d) in `onCreate()`, call `loadState()` first (which now also reads `KEY_THEME`), then call `setTheme(themeKeyToStyleRes(selectedTheme))` before `super.onCreate()` and `setContentView()`; (e) in `loadState()`, read `selectedTheme = p.getString(KEY_THEME, "default") ?: "default"`; (f) in `saveState()`, add `.putString(KEY_THEME, selectedTheme)`.
  Done when: `GrowthActivity.kt` compiles, `setTheme()` is called before `super.onCreate()` and `setContentView()`, `loadState()` reads `KEY_THEME`, `saveState()` writes `KEY_THEME`, and `./gradlew assembleDebug` succeeds.

- [x] 1.7 Wire Spinner in `onCreate()`: (a) find `themeSpinner` by ID; (b) create an `ArrayAdapter` from `R.array.theme_names` with `android.R.layout.simple_spinner_item` and set `simple_spinner_dropdown_item` as dropdown layout; (c) set the adapter on the Spinner; (d) set `themeSpinner.setSelection` based on the current `selectedTheme` key by finding its index in `R.array.theme_keys`; (e) set an `OnItemSelectedListener` that on item selected saves `selectedTheme` to the matching key from `theme_keys`, calls `saveState()`, and calls `recreate()`.
  Done when: app launches and spinner shows the current theme name; selecting "Minimalist Noir" calls `recreate()` and applies Noir visually; selecting "Default" restores the original purple theme; growth state (value, exponent, rate) is preserved across all theme switches; `./gradlew assembleDebug` succeeds.

- [x] 1.8 Verify Phase 1 end-to-end on device or emulator. Run the app and confirm: (1) the Spinner dropdown appears between the growth number and rate controls, listing all 7 options; (2) selecting "Minimalist Noir" applies a black-and-white theme with red accent; (3) selecting "Default" restores the original purple theme; (4) the selected theme persists after killing and relaunching the app; (5) selecting any of the 5 unimplemented themes (Terminal, Ocean, Solar, Nebula, Emerald) falls back to Default appearance without crash or visual glitch; (6) growth counter value, exponent, and rate are preserved across all theme switches.
  Done when: all 6 checks pass on device/emulator.

---

## HUMAN HANDOFF — Phase 1 Gate

Do not proceed to Phase 2 tasks until a human has verified the Phase 1 end-to-end checks above. Sign off that: the dropdown works, Minimalist Noir applies correctly, Default restores correctly, persistence works, unimplemented themes fall back gracefully, and growth state is preserved.

---

## 2. Phase 2 — Terminal / Phosphor theme

- [x] 2.1 Add Terminal / Phosphor color resources to `colors.xml`: `terminal_bg` (#FF0A0E0A), `terminal_surface` (#FF111A11), `phosphor_green` (#FF33FF33), `phosphor_dim` (#FF1A7A1A), `phosphor_ghost` (#FF0D4D0D), `phosphor_bright` (#FF88FF88), `phosphor_amber` (#FFB000), `green_muted` (#FF4D804D), `button_bg` (#FF0D1A0D), `button_border` (#FF1A3D1A). Add `Theme.BasicApp.Terminal` style to `themes.xml` inheriting from `Theme.MaterialComponents.DayNight.NoActionBar` with all attributes per the Terminal spec.
  Done when: `colors.xml` has all 10 Terminal colors and `themes.xml` has `Theme.BasicApp.Terminal` style; `./gradlew assembleDebug` succeeds.

- [x] 2.2 Create `res/drawable/scanlines.xml` — a gradient shape overlay per the Terminal THEME document.
  Done when: `res/drawable/scanlines.xml` exists and `./gradlew assembleDebug` succeeds.

- [x] 2.3 Update `themeKeyToStyleRes` in `GrowthActivity.kt` so `"terminal_phosphor"` maps to `R.style.Theme_BasicApp_Terminal` instead of the Default fallback.
  Done when: selecting "Terminal / Phosphor" from the spinner applies a dark green-tinted background with phosphor green text and amber accent; `./gradlew assembleDebug` succeeds.

- [x] 2.4 Add Terminal runtime effect: pulsing glow on the growth number. When Terminal theme is active, animate the `growthNumber` shadow radius between 4f and 16f over 2 seconds using `ValueAnimator` with `AccelerateDecelerateInterpolator` and `INFINITE` repeat. Store the animator as a field so it can be cancelled. Guard the effect: only start if `selectedTheme == "terminal_phosphor"`. Cancel any running animator in `onPause()`.
  Done when: selecting Terminal theme shows a continuously pulsing green glow on the growth number; switching away from Terminal stops the animator; the glow does not appear under any other theme; `./gradlew assembleDebug` succeeds.

## 3. Phase 2 — Abyssal Ocean theme

- [x] 3.1 Add Abyssal Ocean color resources to `colors.xml`: `abyss_deep` (#FF020B14), `abyss_mid` (#FF071B2E), `abyss_surface` (#FF0C2A45), `biolum_cyan` (#FF00E5FF), `biolum_soft` (#FF0097A7), `biolum_muted` (#FF005662), `deep_coral` (#FFFF6E6E), `jellyfish_pink` (#FFCE93D8), `surface_light` (#FFB2EBF2), `surface_dim` (#FF4B7C8A), `button_abyss` (#FF0A1F33), `button_ripple` (#FF0D3B5C). Add `Theme.BasicApp.Ocean` style to `themes.xml` per the Ocean spec.
  Done when: `colors.xml` has all 12 Ocean colors and `themes.xml` has `Theme.BasicApp.Ocean` style; `./gradlew assembleDebug` succeeds.

- [x] 3.2 Create `res/drawable/biolum_orb.xml` (radial gradient oval per the Ocean THEME document) and `res/drawable/separator_line_top.xml` (1dp top border line using `biolum_muted`).
  Done when: both drawable XML files exist in `res/drawable/` and `./gradlew assembleDebug` succeeds.

- [x] 3.3 Update `themeKeyToStyleRes` so `"abyssal_ocean"` maps to `R.style.Theme_BasicApp_Ocean`.
  Done when: selecting "Abyssal Ocean" from the spinner applies a deep blue-black background with cyan primary and coral accent; `./gradlew assembleDebug` succeeds.

- [x] 3.4 Add Ocean runtime effect: floating bioluminescent particles. When Ocean theme is active, create 15 small `View` particles (2–6dp, using `biolum_orb` drawable with `fern_green` tint for Ocean) that drift upward with random alpha and duration (8–20 seconds), recycling when they exit the top. Guard with `selectedTheme == "abyssal_ocean"`. Remove all particle views from their parent in `onPause()` or on theme switch.
  Done when: selecting Ocean theme shows faint glowing particles drifting upward; switching away removes all particle views from the layout; no particle views remain under any other theme; `./gradlew assembleDebug` succeeds.

## 4. Phase 2 — Solar Flare theme

- [x] 4.1 Add Solar Flare color resources to `colors.xml`: `void_black` (#FF080604), `ember_surface` (#FF1A120A), `ember_light` (#FF2D1F10), `solar_orange` (#FFFF8F00), `flare_yellow` (#FFFFD54F), `flare_white` (#FFFFF8E1), `ember_glow` (#FFBF6000), `magma_red` (#FFFF3D00), `heat_haze` (#FFFFCC80), `ash_gray` (#FF8D7B6A), `button_ember` (#FF1A1005), `button_flare` (#FF3D2200). Add `Theme.BasicApp.Solar` style to `themes.xml` per the Solar spec.
  Done when: `colors.xml` has all 12 Solar colors and `themes.xml` has `Theme.BasicApp.Solar` style; `./gradlew assembleDebug` succeeds.

- [x] 4.2 Create `res/drawable/bg_solar_gradient.xml` — a radial gradient from `ember_surface` at center through `#0D0906` to `void_black` at edges, gradientRadius 400dp, per the Solar THEME document.
  Done when: `res/drawable/bg_solar_gradient.xml` exists and `./gradlew assembleDebug` succeeds.

- [x] 4.3 Update `themeKeyToStyleRes` so `"solar_flare"` maps to `R.style.Theme_BasicApp_Solar`.
  Done when: selecting "Solar Flare" from the spinner applies a warm black background with solar orange primary and near-white flare text; `./gradlew assembleDebug` succeeds.

- [x] 4.4 Add Solar runtime effect: intensity ramp. When Solar theme is active, modify `growthNumber` shadow radius and shadow color based on the current exponent. Map the exponent range [-8, 16] to a 0–1 progress value. Below 0.4 progress use `ember_glow` color and 8f base radius; 0.4–0.7 use `solar_orange` with 8f + progress*32f radius; above 0.7 use `magma_red` with the same formula. Guard with `selectedTheme == "solar_flare"`. Update on each tick. Reset shadow when switching away from Solar theme.
  Done when: selecting Solar theme shows an orange glow on the growth number that intensifies as the value grows; shadow radius increases with exponent; color shifts from ember_glow → solar_orange → magma_red; switching away resets the shadow to default; `./gradlew assembleDebug` succeeds.

## 5. Phase 2 — Cosmic Nebula theme

- [x] 5.1 Add Cosmic Nebula color resources to `colors.xml`: `deep_space` (#FF0A0612), `nebula_dark` (#FF160D24), `cosmos_surface` (#FF1E1030), `nebula_violet` (#FFB388FF), `starlight` (#FFE1BEE7), `stellar_white` (#FFF3E5F5), `void_purple` (#FF7C4DFF), `nebula_rose` (#FFFF80AB), `plasma_blue` (#FF448AFF), `dust_light` (#FFD1C4E9), `dust_dim` (#FF6A5B7A), `button_cosmos` (#FF120A1E), `star_pinpoint` (#FFFFFFFF). Add `Theme.BasicApp.Nebula` style to `themes.xml` per the Nebula spec.
  Done when: `colors.xml` has all 13 Nebula colors and `themes.xml` has `Theme.BasicApp.Nebula` style; `./gradlew assembleDebug` succeeds.

- [x] 5.2 Create `res/drawable/bg_nebula.xml` (radial gradient per the Nebula THEME document) and `res/drawable/star_dot.xml` (2dp white circle oval per the Nebula THEME document).
  Done when: both drawable XML files exist in `res/drawable/` and `./gradlew assembleDebug` succeeds.

- [x] 5.3 Update `themeKeyToStyleRes` so `"cosmic_nebula"` maps to `R.style.Theme_BasicApp_Nebula`.
  Done when: selecting "Cosmic Nebula" from the spinner applies a deep purple-black background with violet primary and rose accent; `./gradlew assembleDebug` succeeds.

- [x] 5.4 Add Nebula runtime effects: (a) twinkling star field — create 40 small `View` dots (1–3dp, white, random position, random alpha 0–0.6) that fade between random alpha values over 2–6 second durations with `AccelerateDecelerateInterpolator`, recycling endlessly; (b) slow nebula color shift — animate the root layout background color between `deep_space` and `nebula_dark` over 15 seconds, infinite loop. Guard both with `selectedTheme == "cosmic_nebula"`. Cancel and clean up all animators and views on theme switch or `onPause()`.
  Done when: selecting Nebula theme shows twinkling white dots across the screen and a subtle purple background pulse; switching away removes all star views and stops the color shift animator; no star views or animators persist under any other theme; `./gradlew assembleDebug` succeeds.

## 6. Phase 2 — Emerald Growth theme

- [x] 6.1 Add Emerald Growth color resources to `colors.xml`: `forest_floor` (#FF0A120A), `canopy_shadow` (#FF0F1F0F), `moss_surface` (#FF162816), `emerald` (#FF00E676), `fern_green` (#FF66BB6A), `shoot_light` (#FFA5D6A7), `deep_forest` (#FF1B5E20), `golden_hour` (#FFFFD740), `rich_earth` (#FF8D6E63), `dew_light` (#FFC8E6C9), `bark_gray` (#FF5D7A5D), `button_moss` (#FF0E1A0E), `button_sprout` (#FF1B3A1B). Add `Theme.BasicApp.Emerald` style to `themes.xml` per the Emerald spec.
  Done when: `colors.xml` has all 13 Emerald colors and `themes.xml` has `Theme.BasicApp.Emerald` style; `./gradlew assembleDebug` succeeds.

- [x] 6.2 Create `res/drawable/bg_forest.xml` (linear gradient per the Emerald THEME document) and `res/drawable/leaf_shape.xml` (vector drawable per the Emerald THEME document).
  Done when: both drawable XML files exist in `res/drawable/` and `./gradlew assembleDebug` succeeds.

- [x] 6.3 Update `themeKeyToStyleRes` so `"emerald_growth"` maps to `R.style.Theme_BasicApp_Emerald`.
  Done when: selecting "Emerald Growth" from the spinner applies a deep forest green background with vivid emerald primary and golden hour accent; `./gradlew assembleDebug` succeeds.

- [x] 6.4 Add Emerald runtime effect: growth-based color shift. When Emerald theme is active, animate `growthNumber` text color from `shoot_light` to `emerald` as the exponent increases (map exponent range [-8, 16] to 0–1 progress, interpolate using `ArgbEvaluator`). Guard with `selectedTheme == "emerald_growth"`. Update on each tick. Reset text color when switching away.
  Done when: selecting Emerald theme shows the growth number starting as a light shoot green that gradually shifts toward vivid emerald as the number grows; switching away restores default text color behavior; no color shift occurs under any other theme; `./gradlew assembleDebug` succeeds.

## 7. Phase 2 — Final verification

- [ ] 7.1 Verify all seven themes work end-to-end. For each theme in the spinner (Default, Minimalist Noir, Terminal / Phosphor, Abyssal Ocean, Solar Flare, Cosmic Nebula, Emerald Growth): select it, confirm the theme applies visually, confirm the spinner reflects the selection, switch to another theme, confirm the switch works, kill and relaunch the app, confirm the last-selected theme persists. Confirm growth state is preserved across all switches. Confirm no runtime effects leak between themes (no leftover particles, animators, or shadow effects from a previous theme).
  Done when: all 7 themes pass visual and functional checks on device/emulator; no crashes; no runtime effect leakage between themes; persistence works for all 7 themes; growth state is preserved across all switches.

---

## HUMAN HANDOFF — Phase 2 Gate (Optional)

Visual QA of each theme on a physical device to confirm colors, shadows, drawables, and runtime effects match the intent of their THEME_*.md documents. The loop can verify functional correctness; aesthetic judgment on physical screens is a human task.