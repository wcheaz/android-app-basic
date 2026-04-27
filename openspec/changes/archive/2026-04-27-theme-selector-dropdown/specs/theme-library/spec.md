## ADDED Requirements

### Requirement: Terminal / Phosphor theme style and colors
The `Theme.BasicApp.Terminal` style SHALL inherit from `Theme.MaterialComponents.DayNight.NoActionBar` and define a CRT-terminal aesthetic with near-black green-tinted background, phosphor green primary (#FF33FF33), amber accent (#FFB000), and monospace font for the growth number. The Terminal color palette SHALL include all colors from the THEME_TERMINAL_PHOSPHOR.md document: `terminal_bg` (#FF0A0E0A), `terminal_surface` (#FF111A11), `phosphor_green` (#FF33FF33), `phosphor_dim` (#FF1A7A1A), `phosphor_ghost` (#FF0D4D0D), `phosphor_bright` (#FF88FF88), `phosphor_amber` (#FFB000), `green_muted` (#FF4D804D), `button_bg` (#FF0D1A0D), `button_border` (#FF1A3D1A). The theme style SHALL set `android:windowBackground` to `terminal_bg`, `android:statusBarColor` to `terminal_bg`, `android:navigationBarColor` to `terminal_bg`, and `android:textColor` to `phosphor_green`.

#### Scenario: Terminal theme applies CRT-style colors
- **WHEN** the user selects "Terminal / Phosphor" from the dropdown
- **THEN** the activity recreates with a near-black green-tinted background, phosphor green text on the growth number, and amber secondary accent

### Requirement: Abyssal Ocean theme style and colors
The `Theme.BasicApp.Ocean` style SHALL inherit from `Theme.MaterialComponents.DayNight.NoActionBar` and define a deep-sea bioluminescent aesthetic with deep ocean black-blue background (#FF020B14), bright cyan primary (#FF00E5FF), and coral accent (#FFFF6E6E). The Ocean color palette SHALL include all colors from the THEME_ABYSSAL_OCEAN.md document: `abyss_deep`, `abyss_mid`, `abyss_surface`, `biolum_cyan`, `biolum_soft`, `biolum_muted`, `deep_coral`, `jellyfish_pink`, `surface_light`, `surface_dim`, `button_abyss`, `button_ripple`. The theme style SHALL set `android:windowBackground` to `abyss_deep`, `android:statusBarColor` to `abyss_deep`, `android:navigationBarColor` to `abyss_deep`, and `android:textColor` to `surface_light`.

#### Scenario: Ocean theme applies deep-sea colors
- **WHEN** the user selects "Abyssal Ocean" from the dropdown
- **THEN** the activity recreates with a deep blue-black background, bright cyan primary elements, and coral accent on buttons

### Requirement: Solar Flare theme style and colors
The `Theme.BasicApp.Solar` style SHALL inherit from `Theme.MaterialComponents.DayNight.NoActionBar` and define a star-intensity aesthetic with warm-tinted black background (#FF080604), solar orange primary (#FFFF8F00), and magma red accent (#FFFF3D00). The Solar color palette SHALL include all colors from the THEME_SOLAR_FLARE.md document: `void_black`, `ember_surface`, `ember_light`, `solar_orange`, `flare_yellow`, `flare_white`, `ember_glow`, `magma_red`, `heat_haze`, `ash_gray`, `button_ember`, `button_flare`. The theme style SHALL set `android:windowBackground` to `void_black`, `android:statusBarColor` to `void_black`, `android:navigationBarColor` to `void_black`, and `android:textColor` to `heat_haze`.

#### Scenario: Solar Flare theme applies warm intense colors
- **WHEN** the user selects "Solar Flare" from the dropdown
- **THEN** the activity recreates with a warm-tinted black background, solar orange primary, and near-white flare text on the growth number

### Requirement: Cosmic Nebula theme style and colors
The `Theme.BasicApp.Nebula` style SHALL inherit from `Theme.MaterialComponents.DayNight.NoActionBar` and define a deep-space nebula aesthetic with near-black purple background (#FF0A0612), soft violet primary (#FFB388FF), and rose/pink accent (#FFFF80AB). The Nebula color palette SHALL include all colors from the THEME_COSMIC_NEBULA.md document: `deep_space`, `nebula_dark`, `cosmos_surface`, `nebula_violet`, `starlight`, `stellar_white`, `void_purple`, `nebula_rose`, `plasma_blue`, `dust_light`, `dust_dim`, `button_cosmos`, `star_pinpoint`. The theme style SHALL set `android:windowBackground` to `deep_space`, `android:statusBarColor` to `deep_space`, `android:navigationBarColor` to `deep_space`, and `android:textColor` to `dust_light`.

#### Scenario: Cosmic Nebula theme applies purple nebula colors
- **WHEN** the user selects "Cosmic Nebula" from the dropdown
- **THEN** the activity recreates with a deep purple-black background, soft violet primary elements, and rose accent

### Requirement: Emerald Growth theme style and colors
The `Theme.BasicApp.Emerald` style SHALL inherit from `Theme.MaterialComponents.DayNight.NoActionBar` and define a living-forest aesthetic with near-black forest green background (#FF0A120A), vivid emerald primary (#FF00E676), and golden hour accent (#FFFFD740). The Emerald color palette SHALL include all colors from the THEME_EMERALD_GROWTH.md document: `forest_floor`, `canopy_shadow`, `moss_surface`, `emerald`, `fern_green`, `shoot_light`, `deep_forest`, `golden_hour`, `rich_earth`, `dew_light`, `bark_gray`, `button_moss`, `button_sprout`. The theme style SHALL set `android:windowBackground` to `forest_floor`, `android:statusBarColor` to `forest_floor`, `android:navigationBarColor` to `forest_floor`, and `android:textColor` to `dew_light`.

#### Scenario: Emerald Growth theme applies forest green colors
- **WHEN** the user selects "Emerald Growth" from the dropdown
- **THEN** the activity recreates with a deep forest green background, vivid emerald primary text, and golden hour accent on buttons

### Requirement: Theme key to style mapping is updated for all five themes
The Kotlin `themeKeyToStyleRes` mapping function SHALL map all seven theme keys to their corresponding style resource IDs: `"default"` → `R.style.Theme_BasicApp`, `"minimalist_noir"` → `R.style.Theme_BasicApp_Noir`, `"terminal_phosphor"` → `R.style.Theme_BasicApp_Terminal`, `"abyssal_ocean"` → `R.style.Theme_BasicApp_Ocean`, `"solar_flare"` → `R.style.Theme_BasicApp_Solar`, `"cosmic_nebula"` → `R.style.Theme_BasicApp_Nebula`, `"emerald_growth"` → `R.style.Theme_BasicApp_Emerald`. No key SHALL fall back to Default — each key SHALL resolve to its own dedicated theme style.

#### Scenario: All five previously-unimplemented themes now apply their own style
- **WHEN** the user selects "Solar Flare" from the dropdown after Phase 2 implementation
- **THEN** the app applies `Theme.BasicApp.Solar` and renders with solar flare colors (not Default fallback)

#### Scenario: Theme key mapping has no Default fallbacks for implemented themes
- **WHEN** any of the seven theme keys is resolved through `themeKeyToStyleRes`
- **THEN** the returned style resource ID corresponds to that theme's own style (not Default), and the app renders that theme's distinct visual appearance

### Requirement: Theme-specific drawables for each art-pass theme
Each theme that requires background drawables or decorative shapes per its THEME_*.md document SHALL have those drawables created in `res/drawable/`. Specifically: Solar Flare SHALL include `bg_solar_gradient.xml` (radial gradient background); Cosmic Nebula SHALL include `bg_nebula.xml` (radial gradient) and `star_dot.xml` (2dp white circle); Emerald Growth SHALL include `bg_forest.xml` (linear gradient) and `leaf_shape.xml` (vector drawable); Terminal/Phosphor SHALL include `scanlines.xml` (gradient overlay shape); Abyssal Ocean SHALL include `biolum_orb.xml` (radial gradient oval) and `separator_line_top.xml` (top-border line). Minimalist Noir SHALL include `hairline_top.xml` (already created in Phase 1).

#### Scenario: Solar Flare theme uses radial gradient background
- **WHEN** Solar Flare theme is active and the layout references `bg_solar_gradient`
- **THEN** the background displays a warm radial gradient from ember_surface at center to void_black at edges

### Requirement: Theme-specific runtime effects are applied conditionally
Each theme MAY define runtime visual effects in Kotlin code. These effects SHALL be guarded by a theme identity check so they only activate when their theme is active. Effects SHALL be cancelled or cleaned up when the theme changes. The specific effects per theme are: Terminal/Phosphor — pulsing glow animation on the growth number (shadow radius oscillation); Abyssal Ocean — floating bioluminescent particle system; Solar Flare — intensity ramp (glow color and radius scale with growth exponent); Cosmic Nebula — twinkling star field and slow nebula color shift animation; Minimalist Noir — accent flash on rate change (already defined, no additional runtime effect beyond color theming).

#### Scenario: Terminal theme shows pulsing glow on growth number
- **WHEN** Terminal / Phosphor theme is active
- **THEN** the growth number displays a continuously pulsing text-shadow glow effect (shadow radius oscillating between 4f and 16f over 2 seconds)

#### Scenario: Switching away from Terminal theme stops pulsing glow
- **WHEN** the user switches from Terminal / Phosphor to Default
- **THEN** the pulsing glow animation is cancelled and does not persist after recreation

#### Scenario: Solar Flare theme ramps glow intensity with growth
- **WHEN** Solar Flare theme is active and the growth number exponent increases
- **THEN** the growth number's shadow radius and color shift from ember_glow to solar_orange to magma_red as the value grows