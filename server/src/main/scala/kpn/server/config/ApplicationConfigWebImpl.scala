package kpn.server.config

import java.io.File

import com.typesafe.config.ConfigFactory
import kpn.core.app.ApplicationConfig
import kpn.core.db.couch.CouchConfig

class ApplicationConfigWebImpl() extends ApplicationConfig {

  val couchConfig: CouchConfig = {
    val configuration = ConfigFactory.parseFile(new File("/kpn/conf/development.conf"))
    val host = configuration.getString("couch.host")
    val port = configuration.getInt("couch.port")
    val user = configuration.getString("couch.user")
    val password = configuration.getString("couch.password")
    val dbname = configuration.getString("couch.database.main")
    val changeDbname = configuration.getString("couch.database.changes")
    val changesetDbname = configuration.getString("couch.database.changesets")
    val poiDbname = configuration.getString("couch.database.pois")
    val userDbname = configuration.getString("couch.database.users")
    val reviewDbname = configuration.getString("couch.database.reviews")
    val taskDbname = configuration.getString("couch.database.tasks")

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
      taskDbname
    )
  }
}
