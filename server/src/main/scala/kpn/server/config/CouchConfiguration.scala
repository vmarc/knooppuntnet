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
  @Value("${couch.database.main:master}") dbname: String,
  @Value("${couch.database.changes:changes}") changeDbname: String,
  @Value("${couch.database.changesets:changesets}") changesetDbname: String,
  @Value("${couch.database.pois:pois}") poiDbname: String,
  @Value("${couch.database.users:users}") userDbname: String,
  @Value("${couch.database.reviews:reviews}") reviewDbname: String,
  @Value("${couch.database.tasks:tasks}") taskDbname: String
) {

  @Bean
  def couchConfig: CouchConfig = {
    CouchConfig(
      host,
      port.toInt,
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

  @Bean
  def couch: Couch = {
    new Couch(system, couchConfig)
  }

  @Bean
  def mainDatabase(couch: Couch): Database = {
    new DatabaseImpl(couch, dbname)
  }

  @Bean
  def changesDatabase(couch: Couch): Database = {
    new DatabaseImpl(couch, changeDbname)
  }

  @Bean
  def changesetDatabase(couch: Couch): Database = {
    new DatabaseImpl(couch, changesetDbname)
  }

  @Bean
  def poiDatabase(couch: Couch): Database = {
    new DatabaseImpl(couch, poiDbname)
  }

  @Bean
  def taskDatabase(couch: Couch): Database = {
    new DatabaseImpl(couch, taskDbname)
  }

  //  def shutDown(): Unit = {
  //    couch.shutdown()
  //    Await.result(system.terminate(), Duration.Inf)
  //    ()
  //  }
}
