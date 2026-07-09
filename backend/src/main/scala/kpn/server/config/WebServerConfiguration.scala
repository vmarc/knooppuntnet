package kpn.server.config

import com.mongodb.client.MongoClients
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import kpn.database.base.Database
import kpn.database.base.MetricsDatabase
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.analysis.location.LocationConfiguration
import kpn.server.analyzer.engine.analysis.location.LocationConfigurationReader
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile(Array("web"))
class WebServerConfiguration {

  @Bean
  def threadMetrics = new JvmThreadMetrics

  @Bean
  def applicationName(@Value("${app.name:server}") value: String): String = {
    value
  }

  @Bean
  def development(@Value("${app.development:false}") value: Boolean): Boolean = {
    value
  }

  @Bean
  def cryptoKey(@Value("${cryptoKey}") value: String): String = {
    value
  }

  @Bean
  def locationConfiguration: LocationConfiguration = {
    new LocationConfigurationReader().read()
  }

  @Bean
  def database(
    @Value("${app.database.url}") url: String,
    @Value("${app.database.name}") name: String,
  ): Database = {
    val mongoClient = MongoClients.create(url)
    new Database(mongoClient.getDatabase(name).withCodecRegistry(Mongo.codecRegistry))
  }

  @Bean
  def metricsDatabase(
    @Value("${app.metrics-database.url}") url: String,
    @Value("${app.metrics-database.name}") name: String,
  ): MetricsDatabase = {
    val mongoClient = MongoClients.create(url)
    new MetricsDatabase(mongoClient.getDatabase(name).withCodecRegistry(Mongo.codecRegistry))
  }
}
