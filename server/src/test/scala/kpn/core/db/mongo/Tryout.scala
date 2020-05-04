package kpn.core.db.mongo

import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import kpn.core.database.doc.NetworkDoc
import kpn.core.db.couch.Couch
import kpn.server.repository.NetworkRepositoryImpl
import org.bson.codecs.configuration.CodecRegistries

import scala.jdk.CollectionConverters._

object Tryout {

  def main(args: Array[String]): Unit = {

    val codecRegistry = CodecRegistries.fromProviders(new JacksonCodecProvider())
    val mongoClient = MongoClients.create("mongodb://localhost:27017")
    val database = mongoClient.getDatabase("tryout").withCodecRegistry(codecRegistry)

    val mongoCollection: MongoCollection[NetworkDoc] = database.getCollection("networks", classOf[NetworkDoc])

    val networks = mongoCollection.find()

    networks.asScala.foreach { networkDoc =>
      val attributes = networkDoc.network.attributes
      println(s"id=${networkDoc._id} ${attributes.name} ${attributes.nodeCount}/${attributes.routeCount}")
    }
    mongoClient.close()
  }


  def populateDatabase(): Unit = {

    Couch.executeIn("localhost", "attic-analysis") { couchDatabase =>
      val repo = new NetworkRepositoryImpl(couchDatabase)
      val networkIds = repo.allNetworkIds()
      val networkInfos = networkIds.take(10).flatMap(repo.network)
      val networkDocs = networkInfos.map(n => NetworkDoc(s"network:${n.id}", n))


      val codecRegistry = CodecRegistries.fromProviders(new JacksonCodecProvider())
      val mongoClient = MongoClients.create("mongodb://localhost:27017")
      val database = mongoClient.getDatabase("tryout").withCodecRegistry(codecRegistry)

      val mongoCollection: MongoCollection[NetworkDoc] = database.getCollection("networks", classOf[NetworkDoc])

      val res = mongoCollection.insertMany(networkDocs.asJava)

      mongoClient.close()
    }
  }

}
