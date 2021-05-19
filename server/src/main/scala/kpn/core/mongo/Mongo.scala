package kpn.core.mongo

import org.bson.codecs.configuration.CodecRegistries
import org.mongodb.scala.MongoClient
import org.mongodb.scala.MongoDatabase

object Mongo {
  def database(mongoClient: MongoClient, databaseName: String): MongoDatabase = {
    val codecRegistry = CodecRegistries.fromProviders(new JacksonCodecProvider())
    mongoClient.getDatabase(databaseName).withCodecRegistry(codecRegistry)
  }
}
