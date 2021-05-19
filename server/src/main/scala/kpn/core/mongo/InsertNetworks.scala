package kpn.core.mongo

import kpn.core.database.Database
import kpn.core.database.doc.NetworkDoc
import kpn.core.db.couch.Couch
import kpn.server.repository.NetworkRepositoryImpl
import org.mongodb.scala.MongoClient

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object InsertNetworks {

  def main(args: Array[String]): Unit = {
    println("Insert networks")
    Couch.executeIn("kpn-database", "analysis") { couchDatabase =>
      val networkDocs = readCouchNetworks(couchDatabase)
      writeMongoNetworks(networkDocs)
      println("Done")
    }
  }

  private def readCouchNetworks(couchDatabase: Database): Seq[NetworkDoc] = {
    val repo = new NetworkRepositoryImpl(couchDatabase)
    val networkIds = repo.allNetworkIds()
    println(s"Collecting details of ${networkIds.size} networks")
    val networkInfos = networkIds.flatMap(repo.network)
    println(s"${networkIds.size} networks collected")
    networkInfos.map(n => NetworkDoc(s"network:${n.id}", n))
  }

  private def writeMongoNetworks(networkDocs: Seq[NetworkDoc]): Unit = {
    val mongoClient = MongoClient("mongodb://kpn-tiles:27017")
    val database = Mongo.database(mongoClient, "tryout")
    val networksCollection = database.getCollection[NetworkDoc]("networks")
    println(s"Insert")
    val insertManyResultFuture = networksCollection.insertMany(networkDocs).toFuture()
    val insertManyResult = Await.result(insertManyResultFuture, Duration(3, TimeUnit.MINUTES))
    println(s"Insert acknowledged: ${insertManyResult.wasAcknowledged}")
    mongoClient.close()
  }
}
