package kpn.core.db.couch

import java.io.File

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_ABSENT
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.typesafe.config.ConfigFactory
import kpn.core.app.ActorSystemConfig
import kpn.core.database.implementation.DatabaseContext
import kpn.core.database.Database
import kpn.core.database.DatabaseImpl
import kpn.server.json.CountryJsonDeserializer
import kpn.server.json.CountryJsonSerializer
import kpn.server.json.FactJsonDeserializer
import kpn.server.json.FactJsonSerializer
import kpn.server.json.NetworkTypeJsonDeserializer
import kpn.server.json.NetworkTypeJsonSerializer
import kpn.server.json.SubsetJsonDeserializer
import kpn.server.json.SubsetJsonSerializer
import kpn.server.json.TagsJsonDeserializer
import kpn.server.json.TagsJsonSerializer
import kpn.server.json.TimestampJsonDeserializer
import kpn.server.json.TimestampJsonSerializer
import kpn.server.json.WayDirectionJsonDeserializer
import kpn.server.json.WayDirectionJsonSerializer
import kpn.shared.Country
import kpn.shared.Fact
import kpn.shared.NetworkType
import kpn.shared.Subset
import kpn.shared.Timestamp
import kpn.shared.data.Tags
import kpn.shared.route.WayDirection
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration
import scala.concurrent.duration.DurationInt

object Couch {

  val uiTimeout: Timeout = Timeout(10.seconds)
  val defaultTimeout: Timeout = Timeout(60.seconds)
  val batchTimeout: Timeout = Timeout(5.minutes)

  def run(f: (ActorSystem, Couch) => Unit): Unit = {

    val system = ActorSystemConfig.actorSystem()
    val couchConfig = config

    try {
      val couch = new Couch(system, couchConfig)
      try {
        f(system, couch)
      } finally {
        couch.shutdown()
      }
    } finally {
      Await.result(system.terminate(), Duration.Inf)
      ()
    }
  }

  def executeIn(databaseName: String)(action: Database => Unit): Unit = {
    executeIn("localhost", databaseName: String)(action: Database => Unit)
  }

  def executeIn(host: String, databaseName: String)(action: Database => Unit): Unit = {
    val couchConfig = config.copy(host = host)
    val tempCouch = new Couch(ActorSystemConfig.actorSystem(), couchConfig)
    val database = new DatabaseImpl(DatabaseContext(tempCouch, couchConfig, objectMapper, databaseName))
    action(database)
  }

  def config: CouchConfig = {
    val properties = {
      val userFile = new File(System.getProperty("user.home") + "/.osm/osm.properties")
      if (userFile.exists()) {
        userFile
      }
      else {
        val currentWorkingDirectoryFile = new File("/kpn/conf/osm.properties")
        if (currentWorkingDirectoryFile.exists()) {
          currentWorkingDirectoryFile
        }
        else {
          val path1 = userFile.getAbsolutePath
          val path2 = currentWorkingDirectoryFile.getAbsolutePath
          val message = s"Couchdb configuration files ('$path1' or '$path2') not found"
          throw new RuntimeException(message)
        }
      }
    }
    try {
      val config = ConfigFactory.parseFile(properties)
      CouchConfig(
        config.getString("couchdb.host"),
        config.getInt("couchdb.port"),
        config.getString("couchdb.user"),
        config.getString("couchdb.password")
      )
    }
    catch {
      case e: Exception =>
        val message = s"Error parsing '${properties.getAbsolutePath}': " + e.getMessage
        throw new RuntimeException(message, e)
    }
  }

  val objectMapper: ObjectMapper = {

    val b = Jackson2ObjectMapperBuilder.json()
    b.serializationInclusion(NON_ABSENT)
    b.annotationIntrospector(new JacksonAnnotationIntrospector)

    b.deserializerByType(classOf[Country], new CountryJsonDeserializer())
    b.serializerByType(classOf[Country], new CountryJsonSerializer())

    b.deserializerByType(classOf[Fact], new FactJsonDeserializer())
    b.serializerByType(classOf[Fact], new FactJsonSerializer())

    b.deserializerByType(classOf[NetworkType], new NetworkTypeJsonDeserializer())
    b.serializerByType(classOf[NetworkType], new NetworkTypeJsonSerializer())

    b.deserializerByType(classOf[Subset], new SubsetJsonDeserializer())
    b.serializerByType(classOf[Subset], new SubsetJsonSerializer())

    b.deserializerByType(classOf[Tags], new TagsJsonDeserializer())
    b.serializerByType(classOf[Tags], new TagsJsonSerializer())

    b.deserializerByType(classOf[Timestamp], new TimestampJsonDeserializer())
    b.serializerByType(classOf[Timestamp], new TimestampJsonSerializer())

    b.deserializerByType(classOf[WayDirection], new WayDirectionJsonDeserializer())
    b.serializerByType(classOf[WayDirection], new WayDirectionJsonSerializer())

    val om: ObjectMapper = b.build()
    om.registerModule(DefaultScalaModule)
    om
  }

}

class Couch(val sys: ActorSystem, val config: CouchConfig) {
  implicit val system: ActorSystem = sys
  implicit val executionContext: ExecutionContext = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  def shutdown(): Unit = {
    Http(system).shutdownAllConnectionPools().andThen { case _ => system.terminate() }

    // TODO AKKA-HTTP shutdown correct ???
    //Http().(system).ask(Http.CloseAll)(15.second).await
    //    Await.result(system.terminate(), Duration.Inf)
    ()
  }

}
