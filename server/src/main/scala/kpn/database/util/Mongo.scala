package kpn.database.util

import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import kpn.core.tools.config.Dirs
import kpn.core.tools.next.database.NextDatabase
import kpn.core.tools.next.database.NextDatabaseImpl
import kpn.database.base.Database
import kpn.database.base.DatabaseImpl
import kpn.database.base.Types.MongoPipeline
import kpn.tools.code.codecs.ScalaBooleanCodec
import kpn.tools.code.codecs.ScalaDoubleCodec
import kpn.tools.code.codecs.ScalaIntegerCodec
import kpn.tools.code.codecs.ScalaLongCodec
import kpn.tools.code.codecs.generated._CodecProvider
import org.bson.BsonDocument
import org.bson.codecs.BooleanCodec
import org.bson.codecs.DoubleCodec
import org.bson.codecs.IntegerCodec
import org.bson.codecs.LongCodec
import org.bson.codecs.MapCodecProvider
import org.bson.codecs.StringCodec
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.conversions.Bson

import java.io.File
import java.io.FileReader
import java.util.Properties

object Mongo {
  val codecRegistry: CodecRegistry = CodecRegistries.fromRegistries(
    CodecRegistries.fromCodecs(
      new StringCodec(),
      new BooleanCodec(),
      new LongCodec(),
      new IntegerCodec(),
      new DoubleCodec(),
      new ScalaBooleanCodec(),
      new ScalaLongCodec(),
      new ScalaIntegerCodec(),
      new ScalaDoubleCodec(),
    ),
    CodecRegistries.fromProviders(
      new _CodecProvider,
      new MapCodecProvider
    ),
    MongoClientSettings.getDefaultCodecRegistry
  )

  def client: MongoClient = {
    MongoClients.create(url)
  }

  def webClient: MongoClient = {
    MongoClients.create(webUrl)
  }

  def executeIn(databaseName: String)(action: Database => Unit): Unit = {
    executeIn(client, databaseName)(action)
  }

  def webExecuteIn(databaseName: String)(action: Database => Unit): Unit = {
    executeIn(webClient, databaseName)(action)
  }

  private def executeIn(mongoClient: MongoClient, databaseName: String)(action: Database => Unit): Unit = {
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

  def nextDatabase(mongoClient: MongoClient, databaseName: String): NextDatabase = {
    new NextDatabaseImpl(mongoClient.getDatabase(databaseName).withCodecRegistry(codecRegistry))
  }

  def url: String = {
    urlProperty("mongodb.url")
  }

  def webUrl: String = {
    urlProperty("web.mongodb.url")
  }

  private def urlProperty(property: String): String = {
    val properties = new File(Dirs.root, "conf/osm.properties")
    val config = new Properties()
    config.load(new FileReader(properties))
    config.getProperty(property)
  }

  def bsonString(bson: Bson): String = {
    val bsonDocument = bson.toBsonDocument(classOf[BsonDocument], codecRegistry)
    bsonDocument.toJson()
  }

  def pipelineString(stages: MongoPipeline): String = {
    val stageStrings = stages.map(bsonString)
    stageStrings.mkString(",\n").split("\n").mkString("[\n  ", "\n  ", "\n]")
  }
}
