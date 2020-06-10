package kpn.core.db.couch

import java.io.File
import java.io.FileReader
import java.util.Properties

import kpn.core.database.Database
import kpn.core.database.DatabaseImpl
import kpn.core.database.implementation.DatabaseContextImpl
import kpn.server.json.Json

object Couch {

  def executeIn(databaseName: String)(action: Database => Unit): Unit = {
    executeIn("localhost", databaseName: String)(action: Database => Unit)
  }

  def executeIn(host: String, databaseName: String)(action: Database => Unit): Unit = {
    val couchConfig = config.copy(host = host)
    val database = new DatabaseImpl(DatabaseContextImpl(couchConfig, Json.objectMapper, databaseName))
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

      val config = new Properties()
      config.load(new FileReader(properties))

      CouchConfig(
        config.getProperty("couchdb.host"),
        config.getProperty("couchdb.port").toInt,
        config.getProperty("couchdb.user"),
        config.getProperty("couchdb.password")
      )
    }
    catch {
      case e: Exception =>
        val message = s"Error parsing '${properties.getAbsolutePath}': " + e.getMessage
        throw new RuntimeException(message, e)
    }
  }

}
