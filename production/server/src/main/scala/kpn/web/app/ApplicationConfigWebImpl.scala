package kpn.web.app

import kpn.core.app.ApplicationConfig
import kpn.core.db.couch.CouchConfig
import play.api.Configuration

class ApplicationConfigWebImpl(configuration: Configuration) extends ApplicationConfig {

  val couchConfig: CouchConfig = {
    val host = configuration.get[String]("couch.host")
    val port = configuration.get[Int]("couch.port")
    val user = configuration.get[String]("couch.user")
    val password = configuration.get[String]("couch.password")
    val dbname = configuration.get[String]("couch.database.main")
    val changeDbname = configuration.get[String]("couch.database.changes")
    val changesetDbname = configuration.get[String]("couch.database.changesets")
    val poiDbname = configuration.get[String]("couch.database.pois")
    val userDbname = configuration.get[String]("couch.database.users")
    val reviewDbname = configuration.get[String]("couch.database.reviews")
    val taskDbname = configuration.get[String]("couch.database.tasks")
    val graphhopperApiKey = configuration.get[String]("graphhopperApiKey")

    CouchConfig(
      host,
      port,
      user,
      password,
      dbname,
      changeDbname,
      changesetDbname,
      poiDbname,
      userDbname,
      reviewDbname,
      taskDbname,
      graphhopperApiKey
    )
  }
}
