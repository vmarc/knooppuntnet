package kpn.core.db.couch

import java.io.File

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import kpn.core.app.ActorSystemConfig

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

  def executeIn(dbname: String)(action: OldDatabase => Unit): Unit = {
    val couchConfig = config.copy(analysisDatabaseName = dbname)
    executeIn(ActorSystemConfig.actorSystem(), couchConfig)(action)
  }

  def executeIn(system: ActorSystem, host: String, dbname: String)(action: OldDatabase => Unit): Unit = {
    val couchConfig = config.copy(host = host, analysisDatabaseName = dbname)
    executeIn(system, couchConfig)(action)
  }

  def executeIn(host: String, dbname: String)(action: OldDatabase => Unit): Unit = {
    val couchConfig = config.copy(host = host, analysisDatabaseName = dbname)
    executeIn(ActorSystemConfig.actorSystem(), couchConfig)(action)
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
        config.getString("couchdb.password"),
        config.getString("couchdb.database.analysis"),
        config.getString("couchdb.database.changes"),
        config.getString("couchdb.database.changesets"),
        config.getString("couchdb.database.pois"),
        config.getString("couchdb.database.tasks")
      )
    }
    catch {
      case e: Exception =>
        val message = s"Error parsing '${properties.getAbsolutePath}': " + e.getMessage
        throw new RuntimeException(message, e)
    }
  }

  private def executeIn(system: ActorSystem, couchConfig: CouchConfig)(action: OldDatabase => Unit): Unit = {
    try {
      val couch = new Couch(system, couchConfig)
      try {
        val database = new OldDatabaseImpl(couch, couchConfig.analysisDatabaseName)
        action(database)
      } finally {
        couch.shutdown()
      }
    } finally {
      Await.result(system.terminate(), Duration.Inf)
      ()
    }
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
