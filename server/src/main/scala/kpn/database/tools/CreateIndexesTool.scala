package kpn.database.tools

import kpn.database.base.Database
import kpn.database.base.DatabaseCollection
import kpn.database.tools.CreateIndexesTool.Index
import kpn.database.util.Mongo
import kpn.core.util.Log
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.IndexOptions
import org.mongodb.scala.model.Indexes

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object CreateIndexesTool {

  object Index {
    def apply(
      collection: DatabaseCollection[_],
      indexName: String,
      fieldNames: String*
    ): Index = {
      val index = Indexes.ascending(fieldNames: _*)
      Index(
        collection: DatabaseCollection[_],
        indexName: String,
        index
      )
    }
  }

  case class Index(
    collection: DatabaseCollection[_],
    indexName: String,
    index: Bson
  )

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test-3") { database =>
      new CreateIndexesTool(database).createIndexes()
    }
  }
}

class CreateIndexesTool(database: Database) {

  private val log = Log(classOf[CreateIndexesTool])

  def createIndexes(): Unit = {
    val indexes = indexConfiguration(database)
    log.info(s"Create ${indexes.size} indexes")
    indexes.foreach(createIndex)
    log.info("Done")
  }

  private def createIndex(index: Index): Unit = {
    Log.context(s"collection: '${index.collection.name}', index: '${index.indexName}'") {
      if (hasIndex(index)) {
        log.info("Index already exists")
      }
      else {
        log.infoElapsed {
          val collection = database.getCollection(index.collection.name)
          val future = collection.createIndex(index.index, IndexOptions().name(index.indexName)).toFuture()
          Await.result(future, Duration(25, TimeUnit.MINUTES))
          ("Created", ())
        }
      }
    }
  }

  private def hasIndex(index: Index): Boolean = {
    val future = index.collection.listIndexes().toFuture()
    val indexDefinitions = Await.result(future, Duration(25, TimeUnit.MINUTES))
    indexDefinitions.exists(_.name == index.indexName)
  }

  private def indexConfiguration(database: Database): Seq[Index] = {
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
        "attributes.networkType",
        "active"
      ),
      Index(
        database.nodes,
        "labels",
        "labels"
      ),
      Index(
        database.nodes,
        "tiles",
        "tiles"
      ),
      Index(
        database.nodeRouteRefs,
        "nodeId-networkType",
        "nodeId",
        "networkType"
      ),
      Index(
        database.routes,
        "labels",
        "labels",
        "_id"
      ),
      Index(
        database.routes,
        "tiles",
        "tiles"
      ),
      Index(
        database.routes,
        "route-node-references",
        "active",
        "nodeRefs"
      ),
      Index(
        database.routes,
        "route-edges",
        "summary.networkType",
        "proposed",
        "_id",
        "edges.pathId",
        "edges.sourceNodeId",
        "edges.sinkNodeId",
        "edges.meters"
      ),
      Index(
        database.nodeRouteRefs,
        "nodeId",
        "nodeId",
        "routeName"
      ),
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
        database.changes,
        "impact-time",
        "impact",
        "key.time.year",
        "key.time.month",
        "key.time.day"
      ),
      Index(
        database.changes,
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
      )
    )
  }
}
