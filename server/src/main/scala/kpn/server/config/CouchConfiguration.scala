package kpn.server.config

import akka.actor.ActorSystem
import com.fasterxml.jackson.databind.ObjectMapper
import kpn.core.db.couch.Couch
import kpn.core.db.couch.CouchConfig
import kpn.core.db.couch.Database
import kpn.core.db.couch.DatabaseContext
import kpn.core.db.couch.DatabaseImpl
import kpn.core.db.couch.OldDatabase
import kpn.core.db.couch.OldDatabaseImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CouchConfiguration(
  system: ActorSystem,
  objectMapper: ObjectMapper,
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
      password
    )
  }

  @Bean
  def couch: Couch = {
    new Couch(system, couchConfig)
  }

  @Bean
  def analysisDatabase(couchConfig: CouchConfig): Database = {
    new DatabaseImpl(DatabaseContext(couchConfig, objectMapper, analysisDatabaseName))
  }

  @Bean
  def changeDatabase(couchConfig: CouchConfig): Database = {
    new DatabaseImpl(DatabaseContext(couchConfig, objectMapper, changeDatabaseName))
  }

  @Bean
  def changesetDatabase(couchConfig: CouchConfig): Database = {
    new DatabaseImpl(DatabaseContext(couchConfig, objectMapper, changesetDatabaseName))
  }

  @Bean
  def poiDatabase(couchConfig: CouchConfig): Database = {
    new DatabaseImpl(DatabaseContext(couchConfig, objectMapper, poiDatabaseName))
  }

  @Bean
  def taskDatabase(couchConfig: CouchConfig): Database = {
    new DatabaseImpl(DatabaseContext(couchConfig, objectMapper, taskDatabaseName))
  }

  @Bean
  def oldAnalysisDatabase(couch: Couch): OldDatabase = {
    new OldDatabaseImpl(couch, analysisDatabaseName)
  }

  @Bean
  def oldChangeDatabase(couch: Couch): OldDatabase = {
    new OldDatabaseImpl(couch, changeDatabaseName)
  }

  @Bean
  def oldChangesetDatabase(couch: Couch): OldDatabase = {
    new OldDatabaseImpl(couch, changesetDatabaseName)
  }

  @Bean
  def oldPoiDatabase(couch: Couch): OldDatabase = {
    new OldDatabaseImpl(couch, poiDatabaseName)
  }

  @Bean
  def oldTaskDatabase(couch: Couch): OldDatabase = {
    new OldDatabaseImpl(couch, taskDatabaseName)
  }

}
