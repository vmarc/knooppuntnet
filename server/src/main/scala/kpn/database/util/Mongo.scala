package kpn.database.util

import kpn.database.base.Database
import kpn.database.base.DatabaseImpl
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.json.JsonWriterSettings
import org.mongodb.scala.MongoClient
import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
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

  def executeIn(databaseName: String)(action: Database => Unit): Unit = {
    val mongoClient = client
    try {
      val mongoDatabase = database(mongoClient, databaseName)
      action(mongoDatabase)
    }
    finally {
      mongoClient.close()
    }
  }

  def database(mongoClient: MongoClient, databaseName: String): Database = {
    new DatabaseImpl(mongoClient.getDatabase(databaseName).withCodecRegistry(codecRegistry))
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
