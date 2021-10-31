package kpn.server.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import kpn.database.base.Database
import kpn.database.base.DatabaseImpl
import kpn.database.base.MetricsDatabase
import kpn.database.base.MetricsDatabaseImpl
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.analysis.location.LocationConfiguration
import kpn.server.analyzer.engine.analysis.location.LocationConfigurationReader
import kpn.server.analyzer.engine.tiles.TileBuilder
import kpn.server.analyzer.engine.tiles.TileFileRepository
import kpn.server.analyzer.engine.tiles.TileFileRepositoryImpl
import kpn.server.analyzer.engine.tiles.vector.VectorTileBuilder
import kpn.server.json.Json
import org.mongodb.scala.MongoClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

import java.util.concurrent.Executor
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy
import scala.concurrent.ExecutionContext

@Configuration
class ServerConfiguration() {

  @Bean
  @Primary
  def objectMapper: ObjectMapper = Json.objectMapper

  @Bean
  def threadMetrics = new JvmThreadMetrics

  @Bean
  def applicationName(@Value("${app.name:server}") value: String): String = {
    value
  }

  @Bean
  def analyzerStatusFile(@Value("${app.analyzer-status:/kpn/status/status}") value: String): String = {
    value
  }

  @Bean
  def analysisExecutionContext(@Value("${app.analyzer-thread-pool-size:9}") poolSize: Int): ExecutionContext = {
    ExecutionContext.fromExecutor(buildExecutor("analysis", poolSize))
  }

  @Bean
  def analysisExecutor(@Value("${app.analyzer-thread-pool-size:9}") poolSize: Int): Executor = {
    buildExecutor("analyzer", poolSize)
  }

  @Bean
  def routeLoaderExecutor(@Value("${app.analyzer-thread-pool-size:4}") poolSize: Int): Executor = {
    buildExecutor("route-loader", poolSize)
  }

  @Bean
  def systemMetricsEnabled(@Value("${app.system-metrics-enabled:false}") value: Boolean): Boolean = {
    value
  }

  @Bean
  def graphLoadEnabled(@Value("${app.graph-load-enabled:false}") value: Boolean): Boolean = {
    value
  }

  @Bean
  def analyzerEnabled(@Value("${app.analyzer-enabled:false}") value: Boolean): Boolean = {
    value
  }

  @Bean
  def analyzerTileUpdateEnabled(@Value("${app.analyzer-tile-update-enabled:false}") value: Boolean): Boolean = {
    value
  }

  @Bean
  def analyzerReload(@Value("${app.analyzer-reload:false}") value: Boolean): Boolean = {
    value
  }

  @Bean
  def vectorTileBuilder: TileBuilder = {
    new VectorTileBuilder()
  }

  @Bean
  def tileRoot(@Value("${app.tile-root:/kpn/tiles}") value: String): String = {
    value
  }

  @Bean
  def rasterTileRepository(tileRoot: String): TileFileRepository = {
    new TileFileRepositoryImpl(tileRoot, "png")
  }

  @Bean
  def vectorTileRepository(tileRoot: String): TileFileRepository = {
    new TileFileRepositoryImpl(tileRoot, "mvt")
  }

  @Bean
  def oauthApplicationKey(@Value("${oauthApplicationKey}") value: String): String = {
    value
  }

  @Bean
  def oauthApplicationSecret(@Value("${oauthApplicationSecret}") value: String): String = {
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
    val mongoClient = MongoClient(url)
    new DatabaseImpl(mongoClient.getDatabase(name).withCodecRegistry(Mongo.codecRegistry))
  }

  @Bean
  def metricsDatabase(
    @Value("${app.metrics-database.url}") url: String,
    @Value("${app.metrics-database.name}") name: String,
  ): MetricsDatabase = {
    val mongoClient = MongoClient(url)
    new MetricsDatabaseImpl(mongoClient.getDatabase(name).withCodecRegistry(Mongo.codecRegistry))
  }

  private def buildExecutor(name: String, poolSize: Int): Executor = {
    val executor = new ThreadPoolTaskExecutor
    executor.setCorePoolSize(poolSize)
    executor.setMaxPoolSize(poolSize)
    executor.setKeepAliveSeconds(0)
    executor.setRejectedExecutionHandler(new CallerRunsPolicy)
    executor.setWaitForTasksToCompleteOnShutdown(true)
    executor.setAwaitTerminationSeconds(60 * 5)
    executor.setThreadNamePrefix(name + "-")
    executor.initialize()
    executor
  }
}
