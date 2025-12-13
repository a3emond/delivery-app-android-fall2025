# Magazine Delivery App – Technical Documentation

## 1. Introduction

This document provides a complete technical documentation for the **Magazine Delivery App**, an Android application developed in Java using SQLite and a fragment‑based single‑activity architecture. The purpose of this document is to formally demonstrate that **all functional, technical, architectural, and UI/UX requirements defined in the mandate are fully respected**, and to expose the internal design choices, data model, navigation flow, and implementation structure.

The documentation is written in a formal and academic tone, aligned with evaluation criteria for Android development courses. English is used for technical clarity; some domain terms remain in English by convention in the programming ecosystem.

------

## 2. Project Overview

### 2.1 Objective

The application allows a magazine and newspaper delivery company to:

- Manage **routes**, **delivery personnel**, **clients**, **subscriptions**, and **products**
- Assign and unassign routes to delivery personnel
- Associate subscriptions (addresses + products) to routes
- Produce multiple **operational lists** (by route, by product, by deliverer, by subscriber)

The system is designed to be **offline‑first**, relying entirely on a local SQLite database.

### 2.2 Platform & Constraints

| Aspect       | Value                               |
| ------------ | ----------------------------------- |
| Platform     | Android                             |
| Language     | Java                                |
| UI           | XML layouts + ViewBinding           |
| Architecture | Single Activity, multiple Fragments |
| Persistence  | SQLite (manual helper, no Room)     |
| Pattern      | Layered architecture                |
| Localization | English + French (fr‑CA)            |

------

## 3. Functional Requirements Coverage

This section maps each mandate requirement to its concrete implementation.

### 3.1 Application Startup Flow

**Requirements respected:**

- Load data from SQLite on startup
- Display a splash screen
- Display an introduction screen with swipe gesture
- Navigate to main menu

**Implementation:**

- `SplashFragment` displays the centered logo and performs initial loading
- `IntroFragment` presents the app description and handles **left swipe gesture**
- Navigation is handled by `NavigationHelper`

```mermaid
flowchart LR
    Splash --> Intro --> MainMenu
```

------

### 3.2 Main Menu

The main menu exposes all required actions:

- Add / Remove Route
- Assign Route
- Add / Remove Subscription
- Add / Remove Deliverer
- List
- Quit

The **Add Deliverer** option is dynamically disabled when all routes already have an assigned deliverer.

------

### 3.3 Route Management

#### Add Route

- Creates a new route with an auto‑generated ID
- Displays confirmation with route number

#### Delete Route

- Validates route existence
- Prevents deletion of invalid routes
- Automatically moves associated subscriptions to **route 0** ("no route")

```mermaid
sequenceDiagram
    User->>UI: Delete Route
    UI->>RouteRepository: delete(routeId)
    RouteRepository->>SubscriptionRepository: moveToRoute0(routeId)
```

------

### 3.4 Route Assignment

**Rules enforced:**

- A route can have **0 or 1 deliverer**
- A deliverer can manage **multiple routes**

Supported actions:

- Assign deliverer to route
- Remove deliverer from route

Errors and confirmations are displayed inline.

------

### 3.5 Subscription Management

#### Add Subscription

- Split screen layout (1/3 actions, 2/3 form)
- Fields:
  - Client
  - Address
  - Product
  - Quantity
  - Route (suggested, overridable)

Route suggestion is handled by a dedicated utility class.

```mermaid
flowchart TD
    Address --> RouteSuggester --> SuggestedRoute
```

#### Remove Subscription

- Lookup by subscription ID
- Display full subscription details
- On deletion, route data is automatically updated

------

### 3.6 Deliverer Management

#### Add Deliverer

- Same split layout as subscription screen
- Fields: name, address, phone

#### Remove Deliverer

- Removes deliverer from all assigned routes
- Routes remain valid but unassigned

------

### 3.7 Listing Features

The **List** screen is split into:

- Left menu: list selectors
- Right content: dynamic fragment replacement

#### Supported Lists

| Category    | View Type               |
| ----------- | ----------------------- |
| Deliverers  | RecyclerView + GridView |
| Subscribers | GridView                |
| Routes      | RecyclerView + GridView |
| Products    | RecyclerView + GridView |

Special handling:

- **Route 0** aggregates all unassigned subscriptions

------

## 4. Data Model

### 4.1 Entities

#### Route

- id
- label
- deliverer_id (nullable)

Special case: `id = 0` represents unassigned subscriptions.

#### Deliverer

- id
- name
- address
- phone

#### Client

- id
- name
- address
- phone

#### Product

- id
- name
- type (MAGAZINE / NEWSPAPER)

#### Subscription

- id
- client_id
- route_id
- address
- product_id
- quantity
- start_date
- end_date

------

### 4.2 Entity Relationships

```mermaid
erDiagram
    DELIVERER ||--o{ ROUTE : serves
    ROUTE ||--o{ SUBSCRIPTION : contains
    CLIENT ||--o{ SUBSCRIPTION : owns
    PRODUCT ||--o{ SUBSCRIPTION : selected
```

------

## 5. SQLite Schema

SQLite is implemented using a manual `SQLiteOpenHelper` (`AppDatabaseHelper`) and an access façade (`AppDatabase`). Repositories execute CRUD operations using SQL statements and map results to POJOs.

### 5.1 Schema Overview

```mermaid
erDiagram
    ROUTE {
        int id
        string label
        int deliverer_id
    }

    DELIVERER {
        int id
        string name
        string address
        string phone
    }

    CLIENT {
        int id
        string name
        string address
        string phone
    }

    PRODUCT {
        int id
        string name
        string type
    }

    SUBSCRIPTION {
        int id
        int client_id
        int route_id
        string address
        int product_id
        int quantity
        date start_date
        date end_date
    }

    DELIVERER ||--o{ ROUTE : assigned
    ROUTE ||--o{ SUBSCRIPTION : includes
    CLIENT ||--o{ SUBSCRIPTION : subscribes
    PRODUCT ||--o{ SUBSCRIPTION : selected
```

### 5.2 Persistence Lifecycle

```mermaid
sequenceDiagram
    autonumber
    MainActivity->>AppDatabase: getInstance(context)
    AppDatabase->>AppDatabaseHelper: openReadable/openWritable
    AppDatabaseHelper-->>AppDatabase: SQLiteDatabase
    MainActivity->>Repositories: initial queries (load views)
    Repositories->>SQLiteDatabase: CRUD and join queries
    SQLiteDatabase-->>Repositories: Cursor / affected rows
    Repositories-->>UI: model objects and status
```

------

## 6. Application Architecture

### 6.1 Layered Architecture

```mermaid
flowchart TD
    UI --> DOMAIN
    DOMAIN --> REPOSITORY
    REPOSITORY --> SQLITE
```

### 6.2 Responsibilities

- **UI Layer**: Fragments, Adapters, ViewBinding
- **Domain Layer**: Plain Java models
- **Repository Layer**: CRUD operations, joins
- **Database Layer**: SQLiteOpenHelper

------

## 6.3 Package Structure Diagram

The following diagram reflects the **actual Java source tree** (packages and sub-packages) as implemented in the project. It emphasizes responsibility boundaries and typical dependency direction.

```mermaid
flowchart TB

    subgraph ROOT[pro.aedev.deliveryapp]
        MainActivity[ui.MainActivity]
    end

    subgraph UI[ui]
        subgraph UI_MAIN[ui.main]
            SplashFragment
            IntroFragment
            MainMenuFragment
        end

        subgraph UI_ROUTE[ui.route]
            subgraph UI_ROUTE_ASSIGN[ui.route.assignview]
                AssignRouteFragment
            end
            subgraph UI_ROUTE_MANAGE[ui.route.manageview]
                ManageRoutesFragment
                ManageRouteFragmentTopSection
                ManageRouteFragmentBottomSection
            end
        end

        subgraph UI_SUB[ui.subscription]
            AddSubscriptionFragment
            RemoveSubscriptionFragment
        end

        subgraph UI_DEL[ui.deliverer]
            AddDelivererFragment
            RemoveDelivererFragment
        end

        subgraph UI_LIST[ui.list]
            ListFragment
            DelivererListFragment
            SubscribersListFragment
            RouteListFragment
            ProductListFragment
        end

        subgraph UI_ADAPTER[ui.adapter]
            RouteListAdapter
            RouteSpinnerAdapter
            DelivererListAdapter
            DelivererSpinnerAdapter
            DelivererDeliveryAdapter
            ClientSpinnerAdapter
            ProductListAdapter
            ProductSpinnerAdapter
            SubscriptionGridAdapter
            SubscriptionSpinnerAdapter
            ProductDeliveryGridAdapter
        end
    end

    subgraph DATA[data]
        subgraph DB[data.db]
            AppDatabase
            AppDatabaseHelper
        end
        subgraph REPO[data.repo]
            ClientRepository
            DelivererRepository
            ProductRepository
            RouteRepository
            SubscriptionRepository
        end
    end

    subgraph MODEL[model]
        Client
        Deliverer
        Product
        Route
        Subscription
    end

    subgraph UTIL[util]
        NavigationHelper
    end

    MainActivity --> UI_MAIN
    UI_MAIN --> UTIL
    UI_MAIN --> DATA

    UI_ROUTE --> UTIL
    UI_ROUTE --> REPO
    UI_SUB --> UTIL
    UI_SUB --> REPO
    UI_DEL --> UTIL
    UI_DEL --> REPO
    UI_LIST --> UTIL
    UI_LIST --> REPO

    UI_ADAPTER --> MODEL
    UI_ADAPTER --> REPO

    REPO --> DB
    REPO --> MODEL
```

------

## 6.4 Class Responsibility Diagram

This diagram summarizes the primary responsibilities and how core classes collaborate at runtime.

```mermaid
flowchart LR
    subgraph UI_LAYER[UI Layer]
        F[Fragments]
        A[Adapters]
        NAV[NavigationHelper]
    end

    subgraph DATA_LAYER[Data Layer]
        R[Repositories]
        DBH[AppDatabaseHelper]
        DBS[AppDatabase]
    end

    subgraph DOMAIN_LAYER[Domain Layer]
        M[Model POJOs]
    end

    F --> NAV
    F --> R
    A --> M
    A --> R

    R --> DBH
    R --> DBS
    R --> M
```

------

## 6.5 Repository-to-Table Mapping Diagram

This diagram documents the intended repository ownership over SQLite tables and the relationships used for list queries.

```mermaid
flowchart TB

    subgraph Repos
        RouteRepository
        DelivererRepository
        ClientRepository
        ProductRepository
        SubscriptionRepository
    end

    subgraph Tables[SQLite Tables]
        routes[(routes)]
        deliverers[(deliverers)]
        clients[(clients)]
        products[(products)]
        subscriptions[(subscriptions)]
    end

    RouteRepository --> routes
    DelivererRepository --> deliverers
    ClientRepository --> clients
    ProductRepository --> products
    SubscriptionRepository --> subscriptions

    subscriptions --- routes
    subscriptions --- clients
    subscriptions --- products
    routes --- deliverers
```

------

## 7. Navigation Structure

### 7.1 High-Level Navigation Flow

```mermaid
flowchart TD
    MainActivity --> Splash
    Splash --> Intro
    Intro --> Menu

    Menu --> ManageRoutes
    Menu --> AssignRoute
    Menu --> AddSubscription
    Menu --> RemoveSubscription
    Menu --> AddDeliverer
    Menu --> RemoveDeliverer
    Menu --> List
    Menu --> Quit
```

Navigation is centralized through `NavigationHelper`, ensuring:

- No fragment directly manipulates fragment transactions
- Consistent back-stack behavior
- Reduced coupling between UI components

------

## 7.2 Fragment Transaction Sequence (Example: Assign Route)

```mermaid
sequenceDiagram
    autonumber
    User->>MainMenuFragment: Tap "Assign Route"
    MainMenuFragment->>NavigationHelper: replace(R.id.fragment_container, AssignRouteFragment)
    NavigationHelper->>MainActivity: FragmentTransaction.commit()
    MainActivity->>AssignRouteFragment: onCreateView()
    AssignRouteFragment->>AssignRouteFragment: bind views (ViewBinding)
```

------

## 7.3 List Screen Internal Navigation

The List screen behaves as a controller that swaps the right-side content based on the selected list type.

```mermaid
sequenceDiagram
    autonumber
    User->>ListFragment: Select "Routes"
    ListFragment->>NavigationHelper: showChild(RouteListFragment)
    NavigationHelper->>ListFragment: ChildFragmentTransaction.commit()
    RouteListFragment->>RouteRepository: getAllRoutesWithDelivererLabel()
    RouteRepository->>AppDatabaseHelper: rawQuery(...)
    AppDatabaseHelper-->>RouteRepository: Cursor
    RouteRepository-->>RouteListFragment: List<Route>
    RouteListFragment-->>User: RecyclerView updated
```

------

## 8. Localization Strategy

- Code identifiers remain in English
- All UI strings are externalized
- Supported locales:
  - Default: English
  - French Canadian: `values-fr-rCA`

------

## 9. UX & UI Compliance

- All screens provide a **Back to menu** action
- RecyclerView is used everywhere except when GridView is explicitly required
- UI layout strictly follows mandate specifications (split screens, panels)
- Form validation and confirmation messages are always visible to the user
- Screenshots confirm visual compliance with the required UX

------

## 9.1 UI–Data Interaction Diagram

```mermaid
sequenceDiagram
    autonumber
    User->>Fragment: Enter data and submit
    Fragment->>Repository: validate input + apply business rule
    Repository->>AppDatabaseHelper: getWritableDatabase()
    AppDatabaseHelper-->>Repository: SQLiteDatabase
    Repository->>SQLiteDatabase: INSERT / UPDATE / DELETE
    SQLiteDatabase-->>Repository: rows affected / error
    Repository-->>Fragment: success / failure
    Fragment-->>User: confirmation or error message
```

------

## 9.2 UI Layout-to-Fragment Mapping

This diagram documents how XML layouts map to fragments and adapters based on the actual `res/layout` tree.

```mermaid
flowchart TB

    subgraph Layouts[res/layout]
        activity_main_xml[activity_main.xml]
        fragment_splash_xml[fragment_splash.xml]
        fragment_intro_xml[fragment_intro.xml]
        fragment_main_menu_xml[fragment_main_menu.xml]
    end

    subgraph Screens[Java UI Components]
        MainActivity
        SplashFragment
        IntroFragment
        MainMenuFragment
    end

    activity_main_xml --> MainActivity
    fragment_splash_xml --> SplashFragment
    fragment_intro_xml --> IntroFragment
    fragment_main_menu_xml --> MainMenuFragment

```

```mermaid
flowchart TB

    subgraph Layouts[res/layout]
        fragment_manage_routes_xml[fragment_manage_routes.xml]
        fragment_manage_top_xml[fragment_manage_route_top_section.xml]
        fragment_manage_bottom_xml[fragment_manage_route_bottom_section.xml]
        fragment_assign_route_xml[fragment_assign_route.xml]

        fragment_add_subscription_xml[fragment_add_subscription.xml]
        fragment_remove_subscription_xml[fragment_remove_subscription.xml]
        dialog_create_client_xml[dialog_create_client.xml]

        fragment_add_deliverer_xml[fragment_add_deliverer.xml]
        fragment_remove_deliverer_xml[fragment_remove_deliverer.xml]

        fragment_list_xml[fragment_list.xml]
        fragment_deliverer_list_xml[fragment_deliverer_list.xml]
        fragment_subscribers_list_xml[fragment_subscribers_list.xml]
        fragment_route_list_xml[fragment_route_list.xml]
        fragment_product_list_xml[fragment_product_list.xml]
    end

    subgraph Screens[Java UI Components]
        ManageRoutesFragment
        ManageRouteFragmentTopSection
        ManageRouteFragmentBottomSection
        AssignRouteFragment

        AddSubscriptionFragment
        RemoveSubscriptionFragment

        AddDelivererFragment
        RemoveDelivererFragment

        ListFragment
        DelivererListFragment
        SubscribersListFragment
        RouteListFragment
        ProductListFragment
    end

    fragment_manage_routes_xml --> ManageRoutesFragment
    fragment_manage_top_xml --> ManageRouteFragmentTopSection
    fragment_manage_bottom_xml --> ManageRouteFragmentBottomSection
    fragment_assign_route_xml --> AssignRouteFragment

    fragment_add_subscription_xml --> AddSubscriptionFragment
    fragment_remove_subscription_xml --> RemoveSubscriptionFragment
    dialog_create_client_xml --> AddSubscriptionFragment

    fragment_add_deliverer_xml --> AddDelivererFragment
    fragment_remove_deliverer_xml --> RemoveDelivererFragment

    fragment_list_xml --> ListFragment
    fragment_deliverer_list_xml --> DelivererListFragment
    fragment_subscribers_list_xml --> SubscribersListFragment
    fragment_route_list_xml --> RouteListFragment
    fragment_product_list_xml --> ProductListFragment

```

```mermaid
flowchart TB

    subgraph Layouts[res/layout]
        item_route_row_xml[item_route_row.xml]
        item_deliverer_row_xml[item_deliverer_row.xml]
        item_product_row_xml[item_product_row.xml]
        item_subscription_grid_xml[item_subscription_grid.xml]
        item_delivery_grid_xml[item_delivery_grid.xml]
        item_product_delivery_grid_xml[item_product_delivery_grid.xml]
    end

    subgraph Adapters[Java Adapters]
        RouteListAdapter
        DelivererListAdapter
        ProductListAdapter
        SubscriptionGridAdapter
        DelivererDeliveryAdapter
        ProductDeliveryGridAdapter
    end

    item_route_row_xml --> RouteListAdapter
    item_deliverer_row_xml --> DelivererListAdapter
    item_product_row_xml --> ProductListAdapter
    item_subscription_grid_xml --> SubscriptionGridAdapter
    item_delivery_grid_xml --> DelivererDeliveryAdapter
    item_product_delivery_grid_xml --> ProductDeliveryGridAdapter

```



------

## 10. Conclusion

The **Magazine Delivery App** fully satisfies all functional, architectural, and technical requirements defined in the mandate. The application demonstrates:

- Strict compliance with all required features
- A clean, layered, and exam-compliant architecture
- Proper use of fragments within a single-activity design
- Correct SQLite persistence without ORM abstraction
- Clear separation of responsibilities across packages
- Robust handling of business rules and edge cases