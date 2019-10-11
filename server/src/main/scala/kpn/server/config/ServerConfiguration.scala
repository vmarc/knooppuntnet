package kpn.server.config

import akka.actor.ActorSystem
import kpn.core.app.ActorSystemConfig
import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import kpn.core.db.couch.DatabaseImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ServerConfiguration {

  @Bean
  def system: ActorSystem = ActorSystemConfig.actorSystem()

  @Bean
  def config = new ApplicationConfigWebImpl() // TODO replace with @Value's

  //  @Bean
  //  private val applicationContext = new ApplicationContext(system, config)

  @Bean
  def couch: Couch = {
    new Couch(system, config.couchConfig)
  }

  @Bean
  def mainDatabase(couch: Couch, config: ApplicationConfigWebImpl): Database = {
    println("CREATE MAIN DATABASE")
    new DatabaseImpl(couch, config.couchConfig.dbname)
  }

  @Bean
  def changesDatabase(couch: Couch, config: ApplicationConfigWebImpl): Database = {
    new DatabaseImpl(couch, config.couchConfig.changeDbname)
  }

  @Bean
  def changesetDatabase(couch: Couch, config: ApplicationConfigWebImpl): Database = {
    new DatabaseImpl(couch, config.couchConfig.changesetDbname)
  }

  @Bean
  def poiDatabase(couch: Couch, config: ApplicationConfigWebImpl): Database = {
    new DatabaseImpl(couch, config.couchConfig.poiDbname)
  }

  @Bean
  def taskDatabase(couch: Couch, config: ApplicationConfigWebImpl): Database = {
    new DatabaseImpl(couch, config.couchConfig.taskDbname)
  }

  //  def shutDown(): Unit = {
  //    couch.shutdown()
  //    Await.result(system.terminate(), Duration.Inf)
  //    ()
  //  }


}
