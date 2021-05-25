package kpn.core.mongo

import kpn.core.database.doc.NetworkDoc
import kpn.server.json.Json
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.model.Aggregates
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoNetworkRepositoryImpl {
}

class MongoNetworkRepositoryImpl(database: MongoDatabase) {

  private val networks = database.getCollection("networks")

  def networkCount(): Long = {
    val networkCountFuture = networks.countDocuments().toFuture()
    Await.result(networkCountFuture, Duration(3, TimeUnit.SECONDS))
  }

  def findAllNetworks(): Seq[String] = {
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
    docs.map { document =>
      val json = document.toJson()
      val networkDoc = Json.objectMapper.readValue(json, classOf[NetworkDoc])
      networkDoc.network.attributes.name
    }
  }

  def findNetworkById(networkId: Long): Option[NetworkDoc] = {
    val future = networks.find[NetworkDoc](equal("_id", s"network:$networkId")).headOption()
    Await.result(future, Duration(5, TimeUnit.SECONDS))
  }

  def findNetworkByName(networkName: String): Option[NetworkDoc] = {
    val future = networks.aggregate[NetworkDoc](
      Seq(
        Aggregates.filter(Filters.eq("network.attributes.name", networkName)),
        Aggregates.project(
          Projections.fields(
            Projections.excludeId(),
            Projections.include("network.attributes"),
          )
        ),
      )
    ).headOption()
    Await.result(future, Duration(60, TimeUnit.SECONDS))
  }
}
