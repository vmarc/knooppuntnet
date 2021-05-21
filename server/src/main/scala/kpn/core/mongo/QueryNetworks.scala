package kpn.core.mongo

import kpn.core.database.doc.NetworkDoc
import kpn.server.json.Json
import org.mongodb.scala.Document
import org.mongodb.scala.MongoClient
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.model.Aggregates
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object QueryNetworks {

  def main(args: Array[String]): Unit = {

    val mongoClient = MongoClient("mongodb://kpn-tiles:27017")
    val database = mongoClient.getDatabase("tryout")
    val networks = database.getCollection("networks")

    networkCount(networks)
    findAllNetworks(networks)
    findNetworkById(networks)
    findNetworkByName(networks)
  }

  private def networkCount(networks: MongoCollection[Document]): Unit = {
    val networkCountFuture = networks.countDocuments().toFuture()
    val networkCount = Await.result(networkCountFuture, Duration(3, TimeUnit.SECONDS))
    println(s"networkCount: $networkCount")
  }

  private def findAllNetworks(networks: MongoCollection[Document]): Unit = {
    println("find all networks")
    val future = networks.aggregate(
      Seq(
        Aggregates.project(
          Projections.fields(
            Projections.excludeId(),
            Projections.include("network.attributes.name"),
          )
        )
      )
    ).toFuture()
    val docs = Await.result(future, Duration(60, TimeUnit.SECONDS))
    val networkNames = docs.map { document =>
      val json = document.toJson
      val networkDoc = Json.objectMapper.readValue(json, classOf[NetworkDoc])
      networkDoc.network.attributes.name
    }

    println(s"network names")
    networkNames.foreach { name =>
      println(s"  $name")
    }
  }

  private def findNetworkById(networks: MongoCollection[Document]): Unit = {
    println("find network by id")
    val future = networks.find(equal("_id", "network:1066154")).first().toFuture()
    val networkDocDocument = Await.result(future, Duration(5, TimeUnit.SECONDS))
    val json = networkDocDocument.toJson
    val networkDoc = Json.objectMapper.readValue(json, classOf[NetworkDoc])
    val attributes = networkDoc.network.attributes
    println(s"  networkScope: ${attributes.networkScope.name}")
    println(s"  networkType: ${attributes.networkType.name}")
    println(s"  name: ${attributes.name}")
  }

  private def findNetworkByName(networks: MongoCollection[Document]): Unit = {
    println("find network by name")
    val future = networks.aggregate(
      Seq(
        Aggregates.filter(Filters.eq("network.attributes.name", "Tholen")),
        Aggregates.project(
          Projections.fields(
            Projections.excludeId(),
            Projections.include("network.attributes"),
          )
        ),
      )
    ).toFuture()
    val documents = Await.result(future, Duration(60, TimeUnit.SECONDS))
    val attributess = documents.map { document =>
      val json = document.toJson
      val networkDoc = Json.objectMapper.readValue(json, classOf[NetworkDoc])
      networkDoc.network.attributes
    }
    attributess.foreach { attributes =>
      println(s"  networkScope: ${attributes.networkScope.name}")
      println(s"  networkType: ${attributes.networkType.name}")
      println(s"  name: ${attributes.name}")
    }
  }
}
