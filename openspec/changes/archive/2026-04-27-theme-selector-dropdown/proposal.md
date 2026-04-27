## Why

Six art-pass themes exist as design documents (Terminal/Phosphor, Abyssal Ocean, Solar Flare, Cosmic Nebula, Emerald Growth, Minimalist Noir) but there is no way to see any of them in the running app. The app currently uses a single hardcoded Material theme with no switching mechanism. Adding an in-app theme dropdown lets the user pick between all six themes at runtime, making the art pass visually evaluable without rebuilds.

## What Changes

This change is delivered in two phases with a human handoff between them.

### Phase 1 — Dropdown mechanism + one theme

- Add a `Spinner` (Material dropdown) to the main layout that lists "Default" plus all six theme names. Only "Default" and "Minimalist Noir" will produce a visual change at this phase; the other five entries will be present in the dropdown but will fall through to Default until Phase 2.
- Implement runtime theme switching by calling `setTheme()` and `recreate()` in `GrowthActivity` when the user selects a theme.
- Persist the selected theme in `SharedPreferences` (using the existing `growth_prefs` file) so the choice survives app restarts.
- Restore the saved theme on app launch before `setContentView()` so the correct theme applies from the first frame.
- Create the Minimalist Noir theme style in `themes.xml` with its color resources in `colors.xml` as the first fully-working art-pass theme. Minimalist Noir is chosen because it is the simplest theme (no gradients, no particle effects, no glow animations — just color swaps and a hairline separator), making it the best candidate to validate that the switching mechanism works end-to-end.
- Add the Minimalist Noir hairline drawable (`hairline_top.xml`).

**Human handoff:** Build, install on device, and verify that: (1) the dropdown appears and lists all seven options; (2) selecting "Minimalist Noir" applies visually distinct colors; (3) selecting "Default" restores the original purple theme; (4) the selected theme persists after killing and relaunching the app; (5) selecting any of the five not-yet-implemented themes falls back to Default gracefully. Stop here and get human sign-off before proceeding.

### Phase 2 — Remaining five themes

- Create the remaining five Android theme styles in `themes.xml`, each derived from its THEME_*.md document, with corresponding color resources in `colors.xml`.
- Create theme-specific drawable resources (gradient backgrounds, decorative shapes) referenced by each theme.
- Add any theme-specific runtime effects described in the THEME_*.md files (pulsing glow, intensity ramp, accent flash) behind a theme-conditional check in `GrowthActivity`.
- Wire the five remaining dropdown entries to their actual theme styles so selection produces the correct visual change.

## Capabilities

### New Capabilities

- `theme-switching`: Runtime theme selection via a dropdown menu, with persistence across app restarts and correct application on activity creation. Includes the dropdown mechanism itself plus the Minimalist Noir theme as the first verified theme.

- `theme-library`: The remaining five art-pass themes (Terminal/Phosphor, Abyssal Ocean, Solar Flare, Cosmic Nebula, Emerald Growth), each wired into the dropdown with its colors, drawables, and optional runtime effects.

### Modified Capabilities

*(none — no existing specs)*

## Impact

### Phase 1

- **GrowthActivity.kt**: Theme application in `onCreate` before `setContentView`, spinner setup and listener, persistence read/write for `selected_theme` key.
- **activity_growth.xml**: Add a `Spinner` above or within `rateControls`; adjust constraints.
- **themes.xml**: Add `Theme.BasicApp.Noir` style plus keep existing `Theme.BasicApp` as Default.
- **colors.xml**: Add Minimalist Noir color resources (true_black, carbon, pure_white, signal_red, etc.).
- **res/drawable/**: Add `hairline_top.xml` for Minimalist Noir.
- **SharedPreferences**: Add `selected_theme` key to existing `growth_prefs`.
- **No new dependencies**: Material Components Spinner and `setTheme()`/`recreate()` are available from existing deps.

### Phase 2

- **themes.xml**: Add five more theme styles (Terminal, Ocean, Solar, Nebula, Emerald).
- **colors.xml**: Add color resources for the remaining five themes.
- **res/drawable/**: Add gradient backgrounds and decorative shapes per theme (bg_solar_gradient, bg_nebula, bg_forest, scanlines, flare_burst, biolum_orb, etc.).
- **GrowthActivity.kt**: Add theme-conditional runtime effects (pulsing glow, intensity ramp, accent flash, particle effects) guarded by theme ID checks.

### Across both phases

- **No breaking changes**: The "Default" option always preserves the current app appearance exactly.

## Non-Goals

- No day/night auto-switching based on system dark mode — the dropdown selection is always manual.
- No per-theme font changes beyond what the THEME_*.md documents specify (e.g., monospace for Terminal only).
- No custom theme editor or color picker.
- No animation crossfade between themes — `recreate()` is acceptable.
- No theme preview before selection — the theme applies immediately on dropdown pick.
- No network-based theme downloading or sharing.
- No migration for any previously stored theme preference (first launch defaults to "Default").

## Human Handoff

**Phase 1 gate (mandatory):** Build and install on a physical device or emulator. Verify that: (1) the dropdown appears and lists all seven options (Default + six theme names); (2) selecting "Minimalist Noir" applies visually distinct black-and-white colors with a single red accent; (3) selecting "Default" restores the original purple theme; (4) the selected theme persists after killing and relaunching the app; (5) selecting any of the five not-yet-implemented themes (Terminal/Phosphor, Abyssal Ocean, Solar Flare, Cosmic Nebula, Emerald Growth) falls back to Default gracefully without crashing or visual glitches. Do not proceed to Phase 2 until this handoff is signed off.

**Phase 2 gate (optional):** Visual QA of each remaining theme on a real device to confirm colors, shadows, drawables, and any runtime effects render as intended per their THEME_*.md documents.