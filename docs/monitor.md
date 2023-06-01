## monitor

### admin scenarios

These scenarios are tested in MonitorUpdaterTest* test classes.

### 01_osm_add

Add non-super route with reference type "osm":

```mermaid
sequenceDiagram
    participant App
    participant Server
    participant Database
    App->>Server: addRoute()
    activate Server
    Server->>Server: analyze()
    Server->>Database: add MonitorRoute
    Server->>Database: add MonitorRouteState
    Server->>Database: add MonitorRouteReference
    Server-->>App: SaveResult
    deactivate Server
```

### 02_osm_add_without_relation_id

Add non-super route with reference type "osm", without relation id unknown:

```mermaid
sequenceDiagram
    participant App
    participant Server
    participant Database
    App->>Server: addRoute()
    activate Server
    Server->>Database: add MonitorRoute
    Server-->>App: SaveResult
    deactivate Server
    App->>Server: updateRoute()
    activate Server
    Server->>Server: analyze()
    Server->>Database: update MonitorRoute
    Server->>Database: add MonitorRouteState
    Server->>Database: add MonitorRouteReference
    Server-->>App: SaveResult
    deactivate Server
```

### 03_osm_add_super_route

Add super route with reference type "osm":

```mermaid
sequenceDiagram
    participant App
    participant Server
    participant Database
    App->>Server: addRoute()
    activate Server
    Server->>Server: analyzeSuperRelation()
    Server->>Server: analyzeSubRelation1()
    Server->>Database: add MonitorRouteState 1
    Server->>Database: add MonitorRouteReference 1
    Server->>Server: analyzeSubRelation2()
    Server->>Database: add MonitorRouteState 2
    Server->>Database: add MonitorRouteReference 2
    Server->>Server: analyzeSubRelation3()
    Server->>Database: add MonitorRouteState 3
    Server->>Database: add MonitorRouteReference 3
    Server->>Database: add MonitorRoute
    Server-->>App: SaveResult
    deactivate Server
```

### 07_gpx_add

Add route with reference type "gpx":

```mermaid
sequenceDiagram
    participant App
    participant Server
    participant Database
    App->>Server: addRoute()
    activate Server
    Server->>Database: add MonitorRoute
    Server-->>App: SaveResult
    deactivate Server
    App->>Server: upload()
    activate Server
    Server->>Database: update MonitorRoute
    Server->>Database: add MonitorRouteReference
    Server-->>App: SaveResult
    deactivate Server
    App->>Server: analyze()
    activate Server
    Server->>Server: analyze()
    Server->>Database: update MonitorRoute
    Server->>Database: add MonitorRouteState
    Server->>Database: update MonitorRouteReference?
    Server-->>App: SaveResult
    deactivate Server
```

### 08_gpx_add_without_relation_id

Add route with reference type "gpx", without relation id:

```mermaid
sequenceDiagram
    participant App
    participant Server
    participant Database
    App->>Server: addRoute()
    activate Server
    Server->>Database: add MonitorRoute
    Server-->>App: SaveResult
    deactivate Server
    App->>Server: upload()
    activate Server
    Server->>Database: update MonitorRoute
    Server->>Database: add MonitorRouteReference
    Server-->>App: SaveResult
    deactivate Server
```

### 09_multi_gpx_add

Add super-route with reference type "multi-gpx":

```mermaid
sequenceDiagram
    participant App
    participant Server
    participant Database
    App->>Server: addRoute()
    activate Server
    Server->>Database: add MonitorRoute
    Server-->>App: SaveResult
    deactivate Server
    App->>Server: uploadSubRelation1()
    activate Server
    Server->>Server: analyze()
    Server->>Database: update MonitorRoute
    Server->>Database: add MonitorRouteState 2
    Server->>Database: add MonitorRouteReference 2
    Server-->>App: SaveResult
    deactivate Server
    App->>Server: uploadSubRelation2()
    activate Server
    Server->>Server: analyze()
    Server->>Database: update MonitorRoute
    Server->>Database: add MonitorRouteState 2
    Server->>Database: add MonitorRouteReference 2
    Server-->>App: SaveResult
    deactivate Server
    App->>Server: uploadSubRelation3()
    activate Server
    Server->>Server: analyze()
    Server->>Database: update MonitorRoute
    Server->>Database: add MonitorRouteState 3
    Server->>Database: add MonitorRouteReference 3
    Server-->>App: SaveResult
    deactivate Server
```

### 03_osm_add_super_route alternative

Add super route with reference type "osm":

```mermaid
sequenceDiagram
    participant App
    participant Server
    participant Database
    App->>Server: addRoute()
    activate Server
    Server-->>App: status()
    Server->>Server: analyzeSuperRelation()
    Server-->>App: status()
    Server->>Server: analyzeSubRelation1()
    Server->>Database: add MonitorRouteState 1
    Server->>Database: add MonitorRouteReference 1
    Server-->>App: status()
    Server->>Server: analyzeSubRelation2()
    Server->>Database: add MonitorRouteState 2
    Server->>Database: add MonitorRouteState 3
    Server-->>App: status()
    Server->>Server: analyzeSubRelation3()
    Server->>Database: add MonitorRouteReference 2
    Server->>Database: add MonitorRouteReference 3
    Server->>Database: add MonitorRoute
    Server->>App: status()
    deactivate Server
```

### 09_multi_gpx_add alternative

Add super-route with reference type "multi-gpx":

```mermaid
sequenceDiagram
    participant App
    participant Server
    participant Database
    App->>Server: addRoute()
    activate Server
    Server-->>App: status()
    Server->>Database: add MonitorRoute
    Server->>App: status()
    deactivate Server
    App->>Server: uploadSubRelation1()
    activate Server
    Server-->>App: status()
    Server->>Server: analyze()
    Server->>Database: update MonitorRoute
    Server->>Database: add MonitorRouteState 2
    Server->>Database: add MonitorRouteReference 2
    Server->>App: status()
    deactivate Server
    App->>Server: uploadSubRelation2()
    activate Server
    Server-->>App: status()
    Server->>Server: analyze()
    Server->>Database: update MonitorRoute
    Server->>Database: add MonitorRouteState 2
    Server->>Database: add MonitorRouteReference 2
    Server->>App: status()
    deactivate Server
    App->>Server: uploadSubRelation3()
    activate Server
    Server-->>App: status()
    Server->>Server: analyze()
    Server->>Database: update MonitorRoute
    Server->>Database: add MonitorRouteState 3
    Server->>Database: add MonitorRouteReference 3
    Server->>App: status()
    deactivate Server
```

