package kpn.core.mongo

import kpn.api.common.common.Ref
import kpn.api.custom.NetworkType
import kpn.core.database.doc.NetworkDoc
import kpn.core.database.doc.NodeDoc
import kpn.server.json.Json
import org.mongodb.scala.MongoClient
import org.mongodb.scala._
import org.mongodb.scala.model.Aggregates
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Projections
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object QueryNodes {

  def main(args: Array[String]): Unit = {

    val mongoClient = MongoClient("mongodb://kpn-tiles:27017")
    val database = mongoClient.getDatabase("tryout")
    val queryNodes = new QueryNodes(database)

    // queryNodes.findNodesByLocation(NetworkType.hiking, "Essen BE")
    queryNodes.findNodeNetworkReferences(3010119246L)
  }
}

class QueryNodes(database: MongoDatabase) {

  def findNodesByLocation(networkType: NetworkType, location: String): Unit = {
    val nodes = database.getCollection("nodes")
    println("nodes by location")
    val future = nodes.aggregate(
      Seq(
        Aggregates.filter(
          Filters.and(
            Filters.eq("node.active", true),
            Filters.eq("node.names.networkType", networkType.name),
            Filters.eq("node.location.names", location)
          )
        ),
        Aggregates.sort(
          orderBy(ascending("node.names.name", "node.id"))
        ),
        Aggregates.project(
          Projections.fields(
            Projections.excludeId(),
          )
        ),
      )
    ).toFuture()
    val documents = Await.result(future, Duration(60, TimeUnit.SECONDS))
    val nodeDocs = documents.map { document =>
      val json = document.toJson
      Json.objectMapper.readValue(json, classOf[NodeDoc])
    }
    nodeDocs.zipWithIndex.foreach { case (nodeDoc, index) =>
      println(s"  ${index + 1} name: ${nodeDoc.node.networkTypeName(networkType)}, id: ${nodeDoc.node.id}")
    }
  }

  def findNodeNetworkReferences(nodeId: Long): Unit = {
    println("find node network references")
    val networks = database.getCollection("networks")
    val future = networks.aggregate(
      Seq(
        Aggregates.filter(
          Filters.and(
            Filters.eq("network.active", true),
            Filters.eq("network.nodeRefs", nodeId),
          )
        ),
        Aggregates.project(
          Projections.fields(
            Projections.excludeId(),
            Projections.include("network.attributes.id"),
            Projections.include("network.attributes.name"),
          )
        ),
      )
    ).toFuture()
    val documents = Await.result(future, Duration(60, TimeUnit.SECONDS))
    val networkRefs = documents.map { document =>
      val json = document.toJson
      val networkDoc = Json.objectMapper.readValue(json, classOf[NetworkDoc])
      val id = networkDoc.network.attributes.id
      val name = networkDoc.network.attributes.name
      Ref(id, name)
    }
    networkRefs.zipWithIndex.foreach { case (ref, index) =>
      println(s"  ${index + 1} ${ref.id} ${ref.name}")
    }
  }
}
