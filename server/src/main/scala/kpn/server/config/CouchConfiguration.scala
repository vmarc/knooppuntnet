package kpn.server.config

import akka.actor.ActorSystem
import kpn.core.db.couch.Couch
import kpn.core.db.couch.CouchConfig
import kpn.core.db.couch.Database
import kpn.core.db.couch.DatabaseImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CouchConfiguration(
  system: ActorSystem,
  @Value("${couch.host:localhost}") host: String,
  @Value("${couch.port:5984}") port: String,
  @Value("${couch.user:user}") user: String,
  @Value("${couch.password:password}") password: String,
  @Value("${couch.database.analysis:analysis}") analysisDatabaseName: String,
  @Value("${couch.database.changes:changes}") changeDatabaseName: String,
  @Value("${couch.database.changesets:changesets}") changesetDatabaseName: String,
  @Value("${couch.database.pois:pois}") poiDatabaseName: String,
  @Value("${couch.database.tasks:tasks}") taskDatabaseName: String
) {

  @Bean
  def couchConfig: CouchConfig = {
    CouchConfig(
      host,
      port.toInt,
      user,
      password,
      analysisDatabaseName,
      changeDatabaseName,
      changesetDatabaseName,
      poiDatabaseName,
      taskDatabaseName
    )
  }

  @Bean
  def couch: Couch = {
    new Couch(system, couchConfig)
  }

  @Bean
  def analysisDatabase(couch: Couch): Database = {
    new DatabaseImpl(couch, analysisDatabaseName)
  }

  @Bean
  def changeDatabase(couch: Couch): Database = {
    new DatabaseImpl(couch, changeDatabaseName)
  }

  @Bean
  def changesetDatabase(couch: Couch): Database = {
    new DatabaseImpl(couch, changesetDatabaseName)
  }

  @Bean
  def poiDatabase(couch: Couch): Database = {
    new DatabaseImpl(couch, poiDatabaseName)
  }

  @Bean
  def taskDatabase(couch: Couch): Database = {
    new DatabaseImpl(couch, taskDatabaseName)
  }

  //  def shutDown(): Unit = {
  //    couch.shutdown()
  //    Await.result(system.terminate(), Duration.Inf)
  //    ()
  //  }
}
