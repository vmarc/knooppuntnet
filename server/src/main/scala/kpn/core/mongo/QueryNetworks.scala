package kpn.core.mongo

import kpn.core.database.doc.NetworkDoc
import org.mongodb.scala.MongoClient
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.model.Aggregates
import org.mongodb.scala.model.Projections

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object QueryNetworks {

  def main(args: Array[String]): Unit = {

    val mongoClient = MongoClient("mongodb://kpn-tiles:27017")
    val database = Mongo.database(mongoClient, "tryout")
    val networks = database.getCollection[NetworkDoc]("networks")

    networkCount(networks)
    findAllNetworks(networks)
  }

  private def networkCount(networks: MongoCollection[NetworkDoc]): Unit = {
    val networkCountFuture = networks.countDocuments().toFuture()
    val networkCount = Await.result(networkCountFuture, Duration(3, TimeUnit.SECONDS))
    println(s"networkCount: $networkCount")
  }

  private def findAllNetworks(networks: MongoCollection[NetworkDoc]): Unit = {
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
    val networkNames = docs.map(_.network.attributes.name)
    println(s"network names")
    networkNames.foreach { name =>
      println(s"  $name")
    }
  }
}
