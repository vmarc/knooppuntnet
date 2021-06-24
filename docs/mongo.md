# MongoDB

This documents the experiments with MongoDB to see whether it would be a good replacment for CouchDB in knooppuntnet.

- [Install](#install)
- [Security](#security)
- [Install Compass](#install-compass)
- [Migration](#migration)
  

## Install
<a name="install"></a>

Using [instructions](https://docs.mongodb.com/manual/tutorial/install-mongodb-on-ubuntu/) on mongodb site.

Import the public key:
```
wget -qO - https://www.mongodb.org/static/pgp/server-4.4.asc | sudo apt-key add -
```

Create a list file for MongoDB
```
echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu focal/mongodb-org/4.4 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-4.4.list
```

Reload local package database
```
sudo apt-get update
```

Install the MongoDB packages.
```
sudo apt-get install -y mongodb-org
```

Install shell:
```
sudo apt-get install -y mongodb-mongosh
```

Establish which init system is used (result: systemd):
```
ps --no-headers -o comm 1
```

Comment out line with bindIp in /etc/mongod.conf to allow connections from other hosts:
```
#  bindIp: 127.0.0.1
```

Start MongoDB:
```
sudo systemctl start mongod
```

Make sure MongoDB will start upon reboot:
```
sudo systemctl enable mongod
```

Verify that MongoDB has started successfully

```
sudo systemctl status mongod
ps -ef | grep mongodb
```

Stop MongoDB
```
sudo systemctl stop mongod
```

Restart MongoDB
```
sudo systemctl restart mongod
```

Mongo shell
```
mongo
```

## Security
<a name="security"></a>

Create admin user:
```
use admin
db.createUser(
  {
    user: "admin",
    pwd: passwordPrompt(),
    roles: [ { role: "userAdminAnyDatabase", db: "admin" }, "readWriteAnyDatabase" ]
  }
)
```

Create app user (logged in as admin user):
```
use tryout
db.createUser(
  {
    user: "kpn-app",
    pwd:  passwordPrompt(),
    roles: [ { role: "readWrite", db: "tryout" } ]
  }
)

db.grantRolesToUser(
    "kpn-app",
    [
      { role: "readWrite", db: "kpn-test" }
    ]
)
```



## Install Compass
<a name="install-compass"></a>

Use [instructions](https://docs.mongodb.com/compass/current/install) on mongodb site.

Download
```
wget https://downloads.mongodb.com/compass/mongodb-compass_1.26.1_amd64.deb
```

Install
```
sudo apt-get install libgconf-2-4
sudo apt --fix-broken install
sudo dpkg -i mongodb-compass_1.26.1_amd64.deb
```

Start

```
mongodb-compass
```


## Migration
<a name="migration"></a>

### Databases and repositories

#### Analysis database

Repositories in the analysis database that have to be migrated together:

AnalysisRepository
- [ ] def saveNetwork(network: Network): Unit
- [ ] def saveIgnoredNetwork(network: NetworkInfo): Unit
- [ ] def saveRoute(route: RouteInfo): Unit
- [ ] def saveNode(node: NodeInfo): Unit
- [ ] def lastUpdated(): Option[Timestamp]
- [ ] def saveLastUpdated(timestamp: Timestamp): Unit

NetworkRepository
- [x]  def allNetworkIds(): Seq[Long]
- [x]  def network(networkId: Long): Option[NetworkInfo]
- [x]  def save(network: NetworkInfo): Unit
- [x]  def elements(networkId: Long): Option[NetworkElements]
- [x]  def saveElements(networkElements: NetworkElements): Unit
- [x]  def gpx(networkId: Long): Option[GpxFile]
- [x]  def saveGpxFile(gpxFile: GpxFile): Unit
- [x]  def networks(subset: Subset, stale: Boolean = true): Seq[NetworkAttributes]
- [x]  def delete(networkId: Long): Unit

RouteRepository
- [x] def allRouteIds(): Seq[Long]
- [x] def save(routes: RouteInfo): Unit
- [x] def saveElements(routeElements: RouteElements): Unit
- [x] def delete(routeIds: Seq[Long]): Unit
- [x] def routeWithId(routeId: Long): Option[RouteInfo]
- [x] def routeElementsWithId(routeId: Long): Option[RouteElements]
- [x] def routesWithIds(routeIds: Seq[Long]): Seq[RouteInfo]
- [ ] def routeReferences(routeId: Long, stale: Boolean = true): RouteReferences
- [ ] def filterKnown(routeIds: Set[Long]): Set[Long]


NodeRepository
- [ ] def allNodeIds(): Seq[Long]
- [ ] def save(nodes: NodeInfo*): Boolean
- [ ] def delete(nodeId: Long): Unit
- [ ] def nodeWithId(nodeId: Long): Option[NodeInfo]
- [ ] def nodesWithIds(nodeIds: Seq[Long], stale: Boolean = true): Seq[NodeInfo]
- [ ] def nodeNetworkReferences(nodeId: Long, stale: Boolean = true): Seq[NodeNetworkReference]
- [ ] def nodeOrphanRouteReferences(nodeId: Long, stale: Boolean = true): Seq[NodeOrphanRouteReference]
- [ ] def filterKnown(nodeIds: Set[Long]): Set[Long]

NodeRouteRepository
- [ ] def save(nodeRoute: NodeRoute): Unit
- [ ] def delete(nodeId: Long, scopedNetworkType: ScopedNetworkType): Unit
- [ ] def nodeRoutes(scopedNetworkType: ScopedNetworkType): Seq[NodeRoute]
- [ ] def nodeRouteReferences(scopedNetworkType: ScopedNetworkType, nodeId: Long): Seq[Ref]
- [ ] def nodesRouteReferences(scopedNetworkType: ScopedNetworkType, nodeIds: Seq[Long]): Seq[NodeRouteRefs]
- [ ] def actualNodeRouteCounts(scopedNetworkType: ScopedNetworkType): Seq[NodeRouteCount]
- [ ] def expectedNodeRouteCounts(scopedNetworkType: ScopedNetworkType): Seq[NodeRouteExpectedCount]


OrphanRepository - prepared
- [ ] def orphanRoutes(subset: Subset): Seq[OrphanRouteInfo]
- [ ] def orphanNodes(subset: Subset): Seq[NodeInfo]


OverviewRepository
- [ ] def figures(stale: Boolean = true): Map[String, Figure]


FactRepository
- [ ] def factsPerNetwork(subset: Subset, fact: Fact, stale: Boolean = true): Seq[NetworkFactRefs]


GraphRepository
- [ ] def graph(networkType: NetworkType): Option[NodeNetworkGraph]


LocationRepository
- [ ] def summary(locationKey: LocationKey): LocationSummary
- [ ] def routesWithoutLocation(networkType: NetworkType): Seq[Ref]
- [ ] def nodes(locationKey: LocationKey, parameters: LocationNodesParameters, stale: Boolean = true): Seq[LocationNodeInfo]
- [ ] def nodeCount(locationKey: LocationKey, locationNodesType: LocationNodesType, stale: Boolean = true): Long
- [ ] def routes(locationKey: LocationKey, parameters: LocationRoutesParameters, stale: Boolean = true): Seq[LocationRouteInfo]
- [ ] def routeCount(locationKey: LocationKey, locationRoutesType: LocationRoutesType, stale: Boolean = true): Long
- [ ] def countryLocations(networkType: NetworkType, country: Country, stale: Boolean = true): Seq[LocationNodeCount]
- [ ] def facts(networkType: NetworkType, locationName: String, stale: Boolean = true): Seq[LocationFact]
- [ ] def factCount(networkType: NetworkType, locationName: String, stale: Boolean = true): Long

TileRepository
- [ ] def nodeIds(networkType: NetworkType, tile: Tile): Seq[Long]
- [ ] def routeIds(networkType: NetworkType, tile: Tile): Seq[Long]

Repository in the analysis database that is standalone and can be migrated separately:

BlackListRepository
- [ ] def get: BlackList
- [ ] def save(blackList: BlackList): Unit


Probably standalone repository that can be migrated separately:

LongdistanceRouteRepository
- [ ] def save(routeInfo: LongdistanceRoute): Unit
- [ ] def routeWithId(routeId: Long): Option[LongdistanceRoute]
- [ ] def all(): Seq[LongdistanceRoute]
- [ ] def saveChange(change: LongdistanceRouteChange): Unit
- [ ] def changes(): Seq[LongdistanceRouteChange]
- [ ] def change(routeId: Long, changeSetId: Long): Option[LongdistanceRouteChange]



#### Change database

2605841 documents:

|type|document|count|
|----|--------|-----|
|network|doc.networkChange|0|
|route|doc.routeChange|0|
|node|doc.nodeChange|0|
|summary|doc.changeSetSummary|0|
|location-summary|doc.locationChangeSetSummary|0|

5 collections?
- network-changes
- route-changes
- node-changes
- changeset-summaries
- location-changeset-summaries


```scala
ChangeSetRepository

  def allNetworkIds(): Seq[Long] // not used
  def allRouteIds(): Seq[Long] // not used
  def allNodeIds(): Seq[Long] // not used
  def allChangeSetIds(): Seq[String] // only used in 1 test

  def saveChangeSetSummary(changeSetSummary: ChangeSetSummary): Unit
  def saveLocationChangeSetSummary(locationChangeSetSummary: LocationChangeSetSummary): Unit
  def saveNetworkChange(networkChange: NetworkChange): Unit
  def saveRouteChange(routeChange: RouteChange): Unit
  def saveNodeChange(nodeChange: NodeChange): Unit

  def changeSet(changeSetId: Long, replicationId: Option[ReplicationId], stale: Boolean = true): Seq[ChangeSetData]
  def changes(changesParameters: ChangesParameters, stale: Boolean = true): Seq[ChangeSetSummary]
  def changesFilter(subset: Option[Subset], year: Option[String], month: Option[String], day: Option[String], stale: Boolean = true): ChangesFilter

  def networkChanges(parameters: ChangesParameters, stale: Boolean = true): Seq[NetworkChange]
  def networkChangesFilter(networkId: Long, year: Option[String], month: Option[String], day: Option[String], stale: Boolean = true): ChangesFilter
  def networkChangesCount(networkId: Long, stale: Boolean = true): Long

  def routeChanges(parameters: ChangesParameters, stale: Boolean = true): Seq[RouteChange]
  def routeChangesFilter(routeId: Long, year: Option[String], month: Option[String], day: Option[String], stale: Boolean = true): ChangesFilter
  def routeChangesCount(routeId: Long, stale: Boolean = true): Long

  def nodeChanges(parameters: ChangesParameters, stale: Boolean = true): Seq[NodeChange]
  def nodeChangesFilter(nodeId: Long, year: Option[String], month: Option[String], day: Option[String], stale: Boolean = true): ChangesFilter
  def nodeChangesCount(nodeId: Long, stale: Boolean = true): Long
```



#### Changeset database

```scala
ChangeSetInfoRepository

  def save(changeSetInfo: ChangeSetInfo): Unit = {}
  def get(changeSetId: Long): Option[ChangeSetInfo] = None
  def all(changeSetIds: Seq[Long], stale: Boolean = true): Seq[ChangeSetInfo] = Seq.empty
  def exists(changeSetId: Long): Boolean = false
  def delete(changeSetId: Long): Unit = {}
```

#### POI database
```scala
PoiRepository -> poiDatabase

  def save(poi: Poi): Unit
  def allPois(stale: Boolean = true): Seq[PoiInfo]
  def nodeIds(stale: Boolean = true): Seq[Long]
  def wayIds(stale: Boolean = true): Seq[Long]
  def relationIds(stale: Boolean = true): Seq[Long]
  def get(poiRef: PoiRef): Option[Poi]
  def delete(poiRef: PoiRef): Unit
  def allTiles(stale: Boolean = true): Seq[String]
  def tilePoiInfos(tileName: String, stale: Boolean = true): Seq[PoiInfo]
```



#### Frontend actions database
```scala
FrontendMetricsRepository --> frontendActionsDatabase

  def saveApiAction(action: ApiAction): Unit
  def saveLogAction(action: LogAction): Unit
  def query(parameters: PeriodParameters, action: String, average: Boolean, stale: Boolean = true): Seq[NameValue]
```


#### Backend actions database
```scala
BackendMetricsRepository --> backendActionsDatabase

  def saveReplicationAction(replicationAction: ReplicationAction): Unit
  def saveUpdateAction(updateAction: UpdateAction): Unit
  def saveAnalysisAction(analysisAction: AnalysisAction): Unit
  def saveSystemStatus(systemStatus: SystemStatus): Unit
  def query(parameters: PeriodParameters, action: String, average: Boolean = false, stale: Boolean = true): Seq[NameValue]
  def lastKnownValue(action: String, stale: Boolean = true): Long
```



#### Monitor database
```scala
MonitorGroupRepository

  def group(groupName: String): Option[MonitorGroup]
  def groups(): Seq[MonitorGroup]
  def groupRoutes(groupName: String): Seq[MonitorRoute]
```

```scala
MonitorRouteRepository

  def route(routeId: Long): Option[MonitorRoute]
  def routeState(routeId: Long): Option[MonitorRouteState]
  def routeReference(routeId: Long, key: String): Option[MonitorRouteReference]
  def routeChange(routeId: Long, changeSetId: Long, replicationNumber: Long): Option[MonitorRouteChange]
  def routeChangeGeometry(routeId: Long, changeSetId: Long, replicationNumber: Long): Option[MonitorRouteChangeGeometry]
  def changesCount(parameters: MonitorChangesParameters): Long
  def changes(parameters: MonitorChangesParameters): Seq[MonitorRouteChange]
  def groupChangesCount(groupName: String, parameters: MonitorChangesParameters): Long
  def groupChanges(groupName: String, parameters: MonitorChangesParameters): Seq[MonitorRouteChange]
  def routeChangesCount(routeId: Long, parameters: MonitorChangesParameters): Long
  def routeChanges(routeId: Long, parameters: MonitorChangesParameters): Seq[MonitorRouteChange]
  def routes(): Seq[MonitorRoute]
```



#### Monitor admin database
```scala
MonitorAdminGroupRepository

  def groups(): Seq[MonitorGroup]
  def group(groupName: String): Option[MonitorGroup]
  def saveGroup(routeGroup: MonitorGroup): Unit
  def deleteGroup(id: String): Unit
  def groupRoutes(groupName: String): Seq[MonitorRoute]
```

```scala
MonitorAdminRouteRepository

  def allRouteIds: Seq[Long]
  def saveRoute(route: MonitorRoute): Unit
  def saveRouteState(routeState: MonitorRouteState): Unit
  def saveRouteReference(routeReference: MonitorRouteReference): Unit
  def saveRouteChange(routeChange: MonitorRouteChange): Unit
  def saveRouteChangeGeometry(routeChangeGeometry: MonitorRouteChangeGeometry): Unit
  def route(routeId: Long): Option[MonitorRoute]
  def routeState(routeId: Long): Option[MonitorRouteState]
  def routeReference(routeId: Long, key: String): Option[MonitorRouteReference]
  def routeChange(changeKey: ChangeKeyI): Option[MonitorRouteChange]
  def routeChangeGeometry(changeKey: ChangeKeyI): Option[MonitorRouteChangeGeometry]
  def routeReferenceKey(routeId: Long): Option[String]
```


#### Task database
```scala
TaskRepository

  def add(key: String): Unit
  def delete(key: String): Unit
  def exists(id: String): Boolean
  def all(prefix: String): Seq[String]
```


## Tips

Merge aggregate in scala code:

```scala
  merge(
    "change-stats-summaries",
    MergeOptions().uniqueIdentifier("_id")
  )
```

Compose concatenated _id in scala code:

```scala
  project(
    fields(
      excludeId(),
      Document("_id" ->
        Document(
          "$concat" ->
            Seq(
              Document("$toString" -> "$_id.impact"),
              Document("$toString" -> ":"),
              Document("$toString" -> "$_id.year"),
              Document("$toString" -> ":"),
              Document("$toString" -> "$_id.month"),
              Document("$toString" -> ":"),
              Document("$toString" -> "$_id.day")
            )
        )
      ),
      computed("impact", "$_id.impact"),
      computed("year", "$_id.year"),
      computed("month", "$_id.month"),
      computed("day", "$_id.day"),
      include("count")
    )
  )
```

## Backup strategy

To backup the complete knooppuntnet database on a regular basis and to keep multiple copies, requires way top much diskspace.  It is mainly the changes collections that are the problem here.

```bash
mongodump --db=tryout
```

```
ll -S dump/tryout/*changes.bson
-rw-rw-r-- 1 marcv marcv  52G Jun 10 21:23 route-changes.bson
-rw-rw-r-- 1 marcv marcv  43G Jun 10 21:23 network-changes.bson
-rw-rw-r-- 1 marcv marcv 435M Jun 10 21:07 changeset-summaries.bson
-rw-rw-r-- 1 marcv marcv 313M Jun 10 21:07 node-changes.bson
-rw-rw-r-- 1 marcv marcv  38M Jun 10 21:07 changeset-comments.bson
```

We can do an incremental backup for these collections, for example per day:

```bash
mongodump --db=tryout --collection=route-changes --query='{"routeChange.key.time.year": 2020, "routeChange.key.time.month": 1, "routeChange.key.time.day": 1}' --gzip --out route-changes-2020-01-01

Note: the dump takes a while (5 mins), it does not seem to use the index.
```

```bash
ll route-changes-2020-01-01/tryout
total 2.2M
-rw-rw-r-- 1 marcv marcv 2.2M Jun 10 21:57 route-changes.bson.gz
-rw-rw-r-- 1 marcv marcv  259 Jun 10 21:52 route-changes.metadata.json.gz
```

Per month:
```bash
ll route-changes-2021-01/tryout/
total 133M
-rw-rw-r-- 1 marcv marcv 133M Jun 10 22:07 route-changes.bson.gz
-rw-rw-r-- 1 marcv marcv  259 Jun 10 22:02 route-changes.metadata.json.gz

23845 documents
```

```bash
ll route-changes-2021-02/tryout/
total 130M
-rw-rw-r-- 1 marcv marcv 130M Jun 10 22:12 route-changes.bson.gz
-rw-rw-r-- 1 marcv marcv  259 Jun 10 22:08 route-changes.metadata.json.gz

23548 documents
```

Per year, with multiple collections in same directory:


```bash
mongodump --db=tryout --collection=route-changes --query='{"routeChange.key.time.year": 2013}' --gzip --out changes-2013
mongodump --db=tryout --collection=network-changes --query='{"networkChange.key.time.year": 2013}' --gzip --out changes-2013
mongodump --db=tryout --collection=node-changes --query='{"nodeChange.key.time.year": 2013}' --gzip --out changes-2013
```

```bash
ll -S changes-2013/tryout
total 462M
-rw-rw-r-- 1 marcv marcv 287M Jun 10 22:19 route-changes.bson.gz
-rw-rw-r-- 1 marcv marcv 174M Jun 10 22:27 network-changes.bson.gz
-rw-rw-r-- 1 marcv marcv 638K Jun 10 22:29 node-changes.bson.gz
-rw-rw-r-- 1 marcv marcv  293 Jun 10 22:20 network-changes.metadata.json.gz
-rw-rw-r-- 1 marcv marcv  259 Jun 10 22:29 node-changes.metadata.json.gz
-rw-rw-r-- 1 marcv marcv  259 Jun 10 22:14 route-changes.metadata.json.gz
```

Incremental restore:
```bash
mongorestore --gzip --nsFrom='tryout.*' --nsTo='tryout2.*' changes-2013
mongorestore --gzip --nsFrom='tryout.*' --nsTo='tryout2.*' route-changes-2021-01
mongorestore --gzip --nsFrom='tryout.*' --nsTo='tryout2.*' route-changes-2021-02
```
