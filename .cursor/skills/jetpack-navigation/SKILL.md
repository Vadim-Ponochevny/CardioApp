---
name: a-jetpack-navigation-3
description: >
  Use this skill whenever the user is working with Jetpack Navigation 3 (Nav3) in Android / Compose — including setup, migration from Navigation 2, back stack management, deep links, Scenes, adaptive layouts, animations, ViewModel integration, conditional navigation, modularization, or returning results. Trigger on any mention of "Navigation 3", "Nav3", "NavEntry", "entryProvider", "NavDisplay", or "back stack" in an Android/Compose context. Also trigger when the user asks how to implement bottom navigation bars, list-detail layouts, two-pane layouts, or multi-back-stack patterns in Compose, even without explicitly naming Navigation 3.
---

# Jetpack Navigation 3

Navigation 3 is the latest Jetpack navigation library for Compose. It replaces Navigation 2 and introduces a fully Compose-first API built around an explicit back stack (a `MutableList<T>`) and `NavEntry`-based destinations.

## Key concepts

| Concept | Description |
|---|---|
| **Back stack** | A plain `MutableList<T>` (usually saved with `rememberSaveable`) holding route keys. You mutate it directly. |
| **NavEntry** | Wraps a destination composable and its lifecycle. Created via `entryProvider { }` DSL or a manual lambda. |
| **NavDisplay** | The host composable that renders the top entry (or entries) of the back stack. |
| **Scene** | Controls *how* entries are laid out — single pane, list-detail, two-pane, dialog, bottom sheet, etc. |

---

## Official documentation

- **Main docs**: https://developer.android.com/guide/navigation/navigation-3
- **Migration Nav2 → Nav3**: https://developer.android.com/guide/navigation/navigation-3/migration-guide
- **Type-safe destinations (Nav2 reference)**: https://developer.android.com/guide/navigation/type-safe-destinations

---

## Recipes — quick reference

When implementing a pattern, refer to the matching recipe URL for a working code example.

### Basic usage
| Pattern | URL |
|---|---|
| Minimal back stack + NavDisplay | https://developer.android.com/guide/navigation/navigation-3/recipes/basic |
| Persistent back stack (saveable) | https://developer.android.com/guide/navigation/navigation-3/recipes/basicsaveable |
| `entryProvider` DSL | https://developer.android.com/guide/navigation/navigation-3/recipes/basicdsl |

### Common UI
| Pattern | URL |
|---|---|
| Bottom nav bar + multiple back stacks | https://developer.android.com/guide/navigation/navigation-3/recipes/common-ui |
| Multiple back stacks (state retained) | https://developer.android.com/guide/navigation/navigation-3/recipes/multiple-backstacks |

### Deep links
| Pattern | URL |
|---|---|
| Parse Intent URL → nav key | https://developer.android.com/guide/navigation/navigation-3/recipes/deeplinks-basic |
| Synthetic back stack + Up navigation | https://developer.android.com/guide/navigation/navigation-3/recipes/deeplinks-advanced |

### Scenes
| Pattern | URL |
|---|---|
| Dialog | https://developer.android.com/guide/navigation/navigation-3/recipes/dialog |
| BottomSheet (custom Scene) | https://developer.android.com/guide/navigation/navigation-3/recipes/bottomsheet |
| List-Detail Scene | https://developer.android.com/guide/navigation/navigation-3/recipes/scenes-listdetail |
| Two-Pane Scene | https://developer.android.com/guide/navigation/navigation-3/recipes/scenes-twopane |

### Material Adaptive
| Pattern | URL |
|---|---|
| Material List-Detail | https://developer.android.com/guide/navigation/navigation-3/recipes/material-listdetail |
| Material Supporting Pane | https://developer.android.com/guide/navigation/navigation-3/recipes/material-supportingpane |

### Animations
| Pattern | URL |
|---|---|
| Override animations (global + per-destination) | https://developer.android.com/guide/navigation/navigation-3/recipes/animations |

### Conditional navigation
| Pattern | URL |
|---|---|
| Auth / onboarding gate | https://developer.android.com/guide/navigation/navigation-3/recipes/conditional |

### Architecture & DI
| Pattern | URL |
|---|---|
| Modular navigation with Hilt/Dagger | https://developer.android.com/guide/navigation/navigation-3/recipes/modular-hilt |
| Modular navigation with Koin | https://developer.android.com/guide/navigation/navigation-3/recipes/modular-koin |

### ViewModel & arguments
| Pattern | URL |
|---|---|
| Pass nav args to ViewModel | https://developer.android.com/guide/navigation/navigation-3/recipes/passingarguments |

### Returning results
| Pattern | URL |
|---|---|
| Results as Events (between NavEntries) | https://developer.android.com/guide/navigation/navigation-3/recipes/results-event |
| Results as State (CompositionLocal) | https://developer.android.com/guide/navigation/navigation-3/recipes/results-state |

---

## Workflow when helping with Nav3

1. **Identify the pattern** the user needs (back stack setup, deep link, Scene, etc.).
2. **Match to a recipe** from the table above — reference the URL or describe the approach from that recipe.
3. **Provide type-safe routes** — define route objects as `data object` or `data class` (for args), not strings.
4. **Show back stack mutation** explicitly: `backStack.add(Route.Detail(id))`, `backStack.removeLastOrNull()`.
5. For **adaptive / multi-pane** layouts, recommend the appropriate Scene or Material Adaptive recipe.
6. For **migration** questions, point to the migration guide and highlight the key breaking changes (no NavHost, no NavController — back stack is the source of truth).

---

## Common pitfalls

- **Don't use NavController** — Nav3 has no NavController. Mutations go directly on the `MutableList`.
- **`rememberSaveable` for the back stack** — required to survive config changes and process death.
- **Scenes are opt-in** — if you only use `NavDisplay` without a Scene, you get single-pane by default.
- **No implicit deep link wiring** — deep links must be parsed manually from the `Intent` and converted to a nav key before pushing onto the back stack.
