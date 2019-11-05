package kpn.core.db.couch

import java.io.File

import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import kpn.core.database.Database
import kpn.core.database.DatabaseImpl
import kpn.core.database.implementation.DatabaseContext
import kpn.server.json.Json

import scala.concurrent.duration.DurationInt

object Couch {

  val uiTimeout: Timeout = Timeout(10.seconds)
  val defaultTimeout: Timeout = Timeout(60.seconds)
  val batchTimeout: Timeout = Timeout(5.minutes)

  def executeIn(databaseName: String)(action: Database => Unit): Unit = {
    executeIn("localhost", databaseName: String)(action: Database => Unit)
  }

  def executeIn(host: String, databaseName: String)(action: Database => Unit): Unit = {
    val couchConfig = config.copy(host = host)
    val database = new DatabaseImpl(DatabaseContext(couchConfig, Json.objectMapper, databaseName))
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

}
