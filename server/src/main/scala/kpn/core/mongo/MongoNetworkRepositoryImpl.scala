package kpn.core.mongo

import kpn.core.database.doc.NetworkDoc
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Filters
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class MongoNetworkRepositoryImpl(database: Database) {

  private val networks = database.database.getCollection("networks")

  def networkCount(): Long = {
    val networkCountFuture = networks.countDocuments().toFuture()
    Await.result(networkCountFuture, Duration(3, TimeUnit.SECONDS))
  }

  def findAllNetworks(): Seq[String] = {
    val pipeline = Seq(
      project(
        fields(
          excludeId(),
          include("network.attributes.name"),
        )
      )
    )
    val future = networks.aggregate[NetworkDoc](pipeline).toFuture()
    val docs = Await.result(future, Duration(60, TimeUnit.SECONDS))
    docs.map { networkDoc =>
      networkDoc.network.attributes.name
    }
  }

  def findNetworkById(networkId: Long): Option[NetworkDoc] = {
    val future = networks.find[NetworkDoc](equal("_id", s"network:$networkId")).headOption()
    Await.result(future, Duration(5, TimeUnit.SECONDS))
  }

  def findNetworkByName(networkName: String): Option[NetworkDoc] = {
    val pipeline = Seq(
      filter(Filters.eq("network.attributes.name", networkName)),
      project(
        fields(
          excludeId(),
          include("network.attributes"),
        )
      )
    )
    val future = networks.aggregate[NetworkDoc](pipeline).headOption()
    Await.result(future, Duration(60, TimeUnit.SECONDS))
  }
}
