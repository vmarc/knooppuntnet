package kpn.core.mongo

import kpn.api.custom.NetworkType
import kpn.core.database.doc.NodeDoc
import kpn.server.json.Json
import org.mongodb.scala.Document
import org.mongodb.scala.MongoClient
import org.mongodb.scala.MongoCollection
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
    val nodes = database.getCollection("nodes")

    findNodesByLocation(nodes, NetworkType.hiking, "Essen BE")
  }

  private def findNodesByLocation(nodes: MongoCollection[Document], networkType: NetworkType, location: String): Unit = {
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
}
