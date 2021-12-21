## How it works

### overview

![overview](knooppuntnet.svg)

- input

  ReplicatorTool

  UpdaterTool

- analysis

  AnalyzerTool

  rules





### Tiles



#### OpenStreetMap tiles

#### Node network tiles

#### Points of interest tiles




#### Database

#### OverpassApi database

- size


#### Couchdb master database

- documents

- views



#### Couchdb changes database

- documents

- views





#### Couchdb tasks database

- ChangesInfoTool

### Points of interest

KnownPois within KnownPoiCacheImpl is used to register all nodes, ways and relations that 
need to be watched while processing minute diff files from OpenStreetMap.

KnownPois is initialized during startup of the analysis server in the @PostConstruct of
KnownPoiCacheImpl. During this initialization the ids of all known pois are read from the
database (MongoQueryPoiElementIds). About 2 million poi ids are loaded in less than 10 seconds.

While processing minute diffs, PoiChangeAnalyzerImpl is used to perform poi analysis.
PoiConfiguration contains the rules that are used to determine whether an OSM element is a
point-of-interest or not. PoiScopeAnalyzerImpl looks at the location of the poi to determine
whether we are interested (the poi has to be within the PoiLocation.simpleBoundingBoxes, we do
not want to collect all pois on the entire world). 

When pois are added, updated or deleted during processing minute diff processing, poi tile
update tasks are generated. These tasks are picked up by PoiTileUpdaterImpl which uses 
PoiTileBuilderImpl to regenerate the affected poi tiles.

The initial load of pois is done with PoiAnalyzerTool. This tool performs overpass queries to 
collect all pois that we are interested in. PoiTileTaskTool can be used to create tasks for
generating all poi tiles after the pois have been initially loaded. Use PoiTileUpdateTool to
actually pick up these tasks and generate the poi tiles.


