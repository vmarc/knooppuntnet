package kpn.server.config

import com.fasterxml.jackson.databind.ObjectMapper
import kpn.core.database.Database
import kpn.core.database.DatabaseImpl
import kpn.core.database.implementation.DatabaseContext
import kpn.core.db.couch.CouchConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CouchConfiguration(
  objectMapper: ObjectMapper,
  @Value("${couch.host:localhost}") host: String,
  @Value("${couch.port:5984}") port: String,
  @Value("${couch.user:user}") user: String,
  @Value("${couch.password:password}") password: String,
  @Value("${couch.database.analysis:analysis}") analysisDatabaseName: String,
  @Value("${couch.database.changes:changes}") changeDatabaseName: String,
  @Value("${couch.database.changesets:changesets}") changesetDatabaseName: String,
  @Value("${couch.database.pois:pois}") poiDatabaseName: String,
  @Value("${couch.database.tasks:tasks}") taskDatabaseName: String,
  @Value("${couch.database.backend-actions:backend-actions}") backendActionsDatabaseName: String,
  @Value("${couch.database.frontend-actions:frontend-actions}") frontendActionsDatabaseName: String
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
  def backendActionsDatabase(couchConfig: CouchConfig): Database = {
    new DatabaseImpl(DatabaseContext(couchConfig, objectMapper, backendActionsDatabaseName))
  }

  @Bean
  def frontendActionsDatabase(couchConfig: CouchConfig): Database = {
    new DatabaseImpl(DatabaseContext(couchConfig, objectMapper, frontendActionsDatabaseName))
  }
}
