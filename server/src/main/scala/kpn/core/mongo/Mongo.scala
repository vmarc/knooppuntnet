package kpn.core.mongo

import org.bson.codecs.configuration.CodecRegistries
import org.mongodb.scala.MongoClient
import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.MongoDatabase

import java.io.File
import java.io.FileReader
import java.util.Properties

object Mongo {

  private val codecRegistry = CodecRegistries.fromRegistries(
    DEFAULT_CODEC_REGISTRY,
    CodecRegistries.fromProviders(new JacksonCodecProvider())
  )

  def client: MongoClient = {
    MongoClient(url)
  }

  def database(mongoClient: MongoClient, databaseName: String): MongoDatabase = {
    mongoClient.getDatabase(databaseName).withCodecRegistry(codecRegistry)
  }

  def url: String = {
    val properties = new File("/kpn/conf/osm.properties")
    val config = new Properties()
    config.load(new FileReader(properties))
    config.getProperty("mongodb.url")
  }
}
