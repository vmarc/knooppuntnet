package kpn.database.index

import kpn.database.base.Database
import org.mongodb.scala.model.Indexes

class IndexConfiguration(database: Database) {

  def indexes: Seq[Index] = {
    Seq(
      baseNetworks,
      baseRoutes,
      changes,
      monitorRouteReferences,
      monitorRouteStates,
      monitorRoutes,
      networkChanges,
      networks,
      nodeChanges,
      nodes,
      pois,
      routeChanges,
      routeTiles,
      routes,
    ).flatten
  }

  private def baseNetworks: Seq[Index] = {
    Seq(
      Index( // support MongoQueryNodeBaseNetworkReferences
        database.baseNetworks,
        "network-node-references",
        "active",
        "nodeIds"
      ),
      Index( // support MongoQueryRouteNetworkReferences
        database.baseNetworks,
        "network-route-references",
        "active",
        "routeIds"
      ),
    )
  }

  private def networks: Seq[Index] = {
    Seq(
      Index(
        database.networks,
        "network-name",
        "active",
        "attributes.name"
      ),
      Index(
        database.networks,
        "network-node-references",
        "active",
        "nodeRefs"
      ),
      Index(
        database.networks,
        "subset-networks",
        "attributes.country",
        "attributes.routeType",
        "active"
      ),
    )
  }

  private def nodes: Seq[Index] = {
    Seq(
      Index(
        database.nodes,
        "labels",
        "active",
        "labels"
      ),
      Index(
        database.nodes,
        "tiles",
        "active",
        "tiles"
      ),
    )
  }

  private def routes: Seq[Index] = {
    Seq(
      Index(
        database.routes,
        "labels",
        "active",
        "labels",
        "_id"
      ),
      Index(
        database.routes,
        "location-routes-page",
        "active",
        "labels",
        "summary.name",
        "summary.id"
      ),
      Index(
        database.routes,
        "tiles",
        "active",
        "tiles"
      ),
      Index( // supports MongoQueryGraphEdges, and other queries that are relevant for node network routes only
        database.routes,
        "node-network-routes",
        "active",
        "summary.nodeNetwork",
      ),
    )
  }

  private def baseRoutes: Seq[Index] = {
    Seq(
      Index(
        database.baseRoutes,
        "route-node-references",
        "active",
        "summary.nodeNetwork",
        "nodeRefs"
      ),
      Index( // support MongoQueryParentRoutes
        database.baseRoutes,
        "sub-routes",
        "subRouteIds",
        "active",
        "_id",
        "summary.name",
      ),
    )
  }

  private def routeTiles: Seq[Index] = {
    Seq(
      Index( // support MongoQueryRouteTileIds
        database.routeTiles,
        "tile",
        "z",
        "x",
        "y",
        "routeTypes",
      ),
    )
  }

  private def networkChanges: Seq[Index] = {
    Seq(
      Index(
        database.networkChanges,
        "time",
        "key.time"
      ),
      Index(
        database.networkChanges,
        "impact",
        "impact"
      ),
      Index(
        database.networkChanges,
        "changeSetId",
        "key.replicationNumber",
        "key.changeSetId"
      ),
      Index( // This index will not be needed anymore if we only have queries based on time instead of timestamp
        database.networkChanges,
        "impact-timestamp",
        Indexes.compoundIndex(
          Indexes.ascending(
            "networkId",
            "impact",
          ),
          Indexes.descending(
            "key.timestamp"
          )
        )
      ),
      Index(
        database.networkChanges,
        "impact-time",
        Indexes.compoundIndex(
          Indexes.ascending(
            "networkId",
            "impact",
          ),
          Indexes.descending(
            "key.time"
          )
        )
      ),
      Index(
        database.networkChanges,
        "networkId-time-impact",
        Indexes.descending(
          "networkId",
          "key.time.year",
          "key.time.month",
          "key.time.day",
          "impact"
        )
      ),
    )
  }

  private def routeChanges: Seq[Index] = {
    Seq(
      Index(
        database.routeChanges,
        "time",
        "key.time"
      ),
      Index(
        database.routeChanges,
        "impact",
        "impact"
      ),
      Index(
        database.routeChanges,
        "changeSetId",
        "key.replicationNumber",
        "key.changeSetId"
      ),
      Index(
        database.routeChanges,
        "routeId-time-impact",
        Indexes.descending(
          "key.elementId",
          "key.time.year",
          "key.time.month",
          "key.time.day",
          "impact"
        )
      ),
    )
  }

  private def nodeChanges: Seq[Index] = {
    Seq(
      Index(
        database.nodeChanges,
        "time",
        "key.time"
      ),
      Index(
        database.nodeChanges,
        "impact",
        "impact"
      ),
      Index(
        database.nodeChanges,
        "changeSetId",
        "key.replicationNumber",
        "key.changeSetId"
      ),
      Index(
        database.nodeChanges,
        "nodeId-time-impact",
        Indexes.descending(
          "key.elementId",
          "key.time.year",
          "key.time.month",
          "key.time.day",
          "impact"
        )
      ),
    )
  }

  private def changes: Seq[Index] = {
    Seq(
      Index(
        database.changes,
        "changes-impact-time",
        Indexes.compoundIndex(
          Indexes.ascending(
            "impact",
          ),
          Indexes.descending(
            "key.time"
          )
        )
      ),
      Index(
        database.changes,
        "changes-time",
        Indexes.descending(
          "key.time"
        )
      ),
      Index(
        database.changes,
        "changes-subset-time",
        Indexes.compoundIndex(
          Indexes.ascending(
            "subsets.country",
            "subsets.routeType",
          ),
          Indexes.descending(
            "key.time"
          )
        )
      ),
      Index(
        database.changes,
        "changes-subset-impact-time",
        Indexes.compoundIndex(
          Indexes.ascending(
            "impact",
            "subsets.country",
            "subsets.routeType",
          ),
          Indexes.descending(
            "key.time"
          )
        )
      ),
      Index(
        database.changes,
        "changeSetId",
        "key.replicationNumber",
        "key.changeSetId"
      ),
      Index(
        database.changes,
        "changes-location",
        Indexes.compoundIndex(
          Indexes.ascending(
            "locations",
            "impact",
          ),
          Indexes.descending(
            "key.time"
          )
        )
      ),
    )
  }

  private def pois: Seq[Index] = {
    Seq(
      Index(
        database.pois,
        "type-id",
        "elementType",
        "elementId"
      ),
      Index(
        database.pois,
        "tiles",
        "tiles"
      ),
      Index(
        database.pois,
        "location.names",
        "layers"
      ),
    )
  }

  private def monitorRoutes: Seq[Index] = {
    Seq(
      Index(
        database.monitorRoutes,
        "groupId-id",
        Indexes.compoundIndex(
          Indexes.ascending(
            "groupId",
          ),
          Indexes.ascending(
            "_id"
          )
        )
      ),
    )
  }

  private def monitorRouteStates: Seq[Index] = {
    Seq(
      Index(
        database.monitorRouteStates,
        "routeId-timestamp",
        Indexes.compoundIndex(
          Indexes.ascending(
            "routeId",
          ),
          Indexes.descending(
            "timestamp"
          )
        )
      ),
    )
  }

  private def monitorRouteReferences: Seq[Index] = {
    Seq(
      Index(
        database.monitorRouteReferences,
        "routeId-created",
        Indexes.compoundIndex(
          Indexes.ascending(
            "routeId",
          ),
          Indexes.descending(
            "created"
          )
        )
      )
    )
  }
}
