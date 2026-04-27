## ADDED Requirements

### Requirement: Theme dropdown selector in main layout
The app SHALL display a Material Components Spinner dropdown in `activity_growth.xml` positioned between `growthNumber` and `rateControls`. The dropdown SHALL list seven items in this exact order: "Default", "Minimalist Noir", "Terminal / Phosphor", "Abyssal Ocean", "Solar Flare", "Cosmic Nebula", "Emerald Growth". The spinner SHALL use `Widget.MaterialComponents.Spinner.Underlined` style. The spinner's background tint SHALL be derived from the active theme's `colorPrimary`.

#### Scenario: Spinner is visible on app launch
- **WHEN** the app launches with any theme
- **THEN** a Spinner dropdown is visible between the growth number display and the rate controls

#### Scenario: Spinner lists all seven theme options
- **WHEN** the user taps the Spinner
- **THEN** a dropdown popup appears showing seven items: Default, Minimalist Noir, Terminal / Phosphor, Abyssal Ocean, Solar Flare, Cosmic Nebula, Emerald Growth

### Requirement: Runtime theme application on activity creation
The app SHALL read the saved theme preference from SharedPreferences before the view hierarchy is created. In `onCreate()`, the app SHALL call `setTheme(resId)` with the resolved style resource ID before calling `super.onCreate()` and `setContentView()`. The mapping from preference key to style resource ID SHALL be: `"default"` → `Theme.BasicApp`, `"minimalist_noir"` → `Theme.BasicApp.Noir`. Any unrecognized or missing preference value SHALL resolve to `Theme.BasicApp`.

#### Scenario: App launches with no saved theme preference
- **WHEN** the app launches and `selected_theme` is not present in SharedPreferences
- **THEN** the app applies `Theme.BasicApp` (the Default theme) and renders with the original purple appearance

#### Scenario: App launches with saved Minimalist Noir preference
- **WHEN** the app launches and `selected_theme` is `"minimalist_noir"` in SharedPreferences
- **THEN** the app applies `Theme.BasicApp.Noir` before creating views and renders with black background, white text, and red accent

#### Scenario: App launches with unrecognized preference value
- **WHEN** the app launches and `selected_theme` is `"some_unknown_value"` in SharedPreferences
- **THEN** the app falls back to `Theme.BasicApp` without crashing

### Requirement: Immediate theme switch on dropdown selection
When the user selects a theme from the Spinner, the app SHALL immediately save the selected theme key to SharedPreferences and call `recreate()` to apply the new theme. The activity SHALL be destroyed and re-created with the new theme.

#### Scenario: User selects Minimalist Noir from dropdown
- **WHEN** the user selects "Minimalist Noir" from the Spinner
- **THEN** the app saves `"minimalist_noir"` to `selected_theme` in SharedPreferences, calls `recreate()`, and the re-created activity applies `Theme.BasicApp.Noir`

#### Scenario: User switches back to Default from another theme
- **WHEN** the user selects "Default" from the Spinner while a different theme is active
- **THEN** the app saves `"default"` to `selected_theme`, calls `recreate()`, and the re-created activity applies `Theme.BasicApp` with the original purple appearance

### Requirement: Theme preference persists across app restarts
The app SHALL store the selected theme key as a String in the existing `growth_prefs` SharedPreferences file under the key `"selected_theme"`. The default value SHALL be `"default"`. The preference SHALL survive app kill and relaunch. The preference SHALL be saved both immediately on spinner selection and during `onPause()` via the existing `saveState()` method.

#### Scenario: Theme survives app kill and relaunch
- **WHEN** the user selects "Minimalist Noir", then kills the app from the recent apps tray, then relaunches the app
- **THEN** the app launches with Minimalist Noir applied

#### Scenario: Theme preference coexists with existing growth state
- **WHEN** the user changes the theme and also changes the growth rate
- **THEN** both the theme preference and the growth state (value, exponent, rate index) are correctly saved and restored independently

### Requirement: Minimalist Noir theme renders with correct colors and chrome
The `Theme.BasicApp.Noir` style SHALL inherit from `Theme.MaterialComponents.DayNight.NoActionBar` and set the following: `colorPrimary` to pure white, `colorPrimaryVariant` to silver, `colorOnPrimary` to true black, `colorSecondary` to signal red, `colorSecondaryVariant` to signal dim, `colorOnSecondary` to pure white, `android:windowBackground` to true black, `android:statusBarColor` to true black, `android:navigationBarColor` to true black, `android:textColor` to pure white. The Noir color palette SHALL include: `true_black` (#FF000000), `carbon` (#FF121212), `carbon_light` (#FF1E1E1E), `pure_white` (#FFFFFFFF), `silver` (#FFB0B0B0), `ghost_gray` (#FF666666), `signal_red` (#FFFF1744), `signal_dim` (#FFB71C1C), `hairline` (#FF1C1C1C), `mid_gray` (#FF888888).

#### Scenario: Minimalist Noir theme displays correct dark appearance
- **WHEN** Minimalist Noir theme is active
- **THEN** the window background is true black (#FF000000), the status bar is true black, the navigation bar is true black, and the growth number text is pure white (#FFFFFFFF)

#### Scenario: Minimalist Noir theme uses signal red as accent
- **WHEN** Minimalist Noir theme is active
- **THEN** `colorSecondary` resolves to signal red (#FFFF1744) and buttons or accent elements use this color

### Requirement: Unimplemented themes fall back to Default
In Phase 1, selecting "Terminal / Phosphor", "Abyssal Ocean", "Solar Flare", "Cosmic Nebula", or "Emerald Growth" from the Spinner SHALL save the corresponding key to SharedPreferences but SHALL apply `Theme.BasicApp` visually. The app SHALL NOT crash, show an error, or display a broken appearance for these selections.

#### Scenario: User selects an unimplemented theme
- **WHEN** the user selects "Solar Flare" from the Spinner in Phase 1
- **THEN** the app saves `"solar_flare"` to SharedPreferences, recreates, and renders with the Default purple appearance (no crash, no broken layout)

#### Scenario: Previously selected unimplemented theme persists correctly
- **WHEN** the user previously selected "Cosmic Nebula", killed the app, and relaunches in Phase 1
- **THEN** the app reads `"cosmic_nebula"` from SharedPreferences, resolves it to `Theme.BasicApp` as fallback, and renders Default appearance

### Requirement: Growth state is preserved across theme changes
When `recreate()` is called for a theme change, the growth value, exponent, and rate index SHALL be preserved. The `onPause()` callback SHALL save all state before recreation. The re-created Activity SHALL restore all state from SharedPreferences in `loadState()`.

#### Scenario: Growth counter continues after theme switch
- **WHEN** the growth counter is at 1.23456789e10 and the user switches to Minimalist Noir
- **THEN** after recreation, the growth counter resumes at the same value with the same rate

### Requirement: Spinner reflects current theme after recreation
After `recreate()`, the Spinner SHALL show the currently active theme as its selected item. The Spinner selection SHALL be initialized from the saved `selected_theme` preference during `onCreate()`.

#### Scenario: Spinner shows active theme after recreation
- **WHEN** the user selects "Minimalist Noir" and the activity recreates
- **THEN** the Spinner displays "Minimalist Noir" as the selected item