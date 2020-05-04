package kpn.core.db.mongo

import java.util.concurrent.TimeUnit

import kpn.core.database.doc.NetworkDoc
import kpn.core.db.couch.Couch
import kpn.server.repository.NetworkRepositoryImpl
import org.bson.codecs.configuration.CodecRegistries
import org.mongodb.scala.MongoClient

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Tryout {

  def main(args: Array[String]): Unit = {

    // populateDatabase()

    val codecRegistry = CodecRegistries.fromProviders(new JacksonCodecProvider())
    val mongoClient = MongoClient()

    println("databases")
    val databasesFuture = mongoClient.listDatabaseNames().toFuture()
    val databases = Await.result(databasesFuture, Duration(10, TimeUnit.SECONDS))
    databases.foreach(println)

    val database = mongoClient.getDatabase("tryout").withCodecRegistry(codecRegistry)
    val mongoCollection = database.getCollection[NetworkDoc]("networks")

    val networksFuture = mongoCollection.find().toFuture()
    val networks = Await.result(networksFuture, Duration(10, TimeUnit.SECONDS))
    networks.foreach { networkDoc =>
      val attributes = networkDoc.network.attributes
      println(s"id=${networkDoc._id} ${attributes.name} ${attributes.nodeCount}/${attributes.routeCount}")
    }

    println("done")
    mongoClient.close()
  }

  def populateDatabase(): Unit = {

    Couch.executeIn("localhost", "attic-analysis") { couchDatabase =>
      val repo = new NetworkRepositoryImpl(couchDatabase)
      val networkIds = repo.allNetworkIds()
      val networkInfos = networkIds.take(50).flatMap(repo.network)
      val networkDocs = networkInfos.map(n => NetworkDoc(s"network:${n.id}", n))

      val codecRegistry = CodecRegistries.fromProviders(new JacksonCodecProvider())
      val mongoClient = MongoClient()
      val database = mongoClient.getDatabase("tryout").withCodecRegistry(codecRegistry)

      val mongoCollection = database.getCollection[NetworkDoc]("networks")

      val insertFuture = mongoCollection.insertMany(networkDocs).toFuture()
      val result = Await.result(insertFuture, Duration(10, TimeUnit.SECONDS))

      println("insert acknowledged = " + result.wasAcknowledged())

      mongoClient.close()
    }
  }

}
