package kpn.core.mongo

import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.json.JsonWriterSettings
import org.mongodb.scala.MongoClient
import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.bson.conversions.Bson

import java.io.File
import java.io.FileReader
import java.util.Properties

object Mongo {

  private val jsonWriterSettings = JsonWriterSettings.builder().indent(true).build()

  val codecRegistry: CodecRegistry = CodecRegistries.fromRegistries(
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

  def bsonString(bson: Bson): String = {
    val bsonDocument = bson.toBsonDocument(classOf[BsonDocument], codecRegistry)
    bsonDocument.toJson(jsonWriterSettings)
  }

  def pipelineString(stages: Seq[Bson]): String = {
    val stageStrings = stages.map(bsonString)
    stageStrings.mkString(",\n").split("\n").mkString("[\n  ", "\n  ", "\n]")
  }

}
