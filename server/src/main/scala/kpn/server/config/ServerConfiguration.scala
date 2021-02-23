package kpn.server.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import kpn.server.analyzer.engine.analysis.location.LocationConfiguration
import kpn.server.analyzer.engine.analysis.location.LocationConfigurationReader
import kpn.server.analyzer.engine.tiles.TileBuilder
import kpn.server.analyzer.engine.tiles.TileFileRepository
import kpn.server.analyzer.engine.tiles.TileFileRepositoryImpl
import kpn.server.analyzer.engine.tiles.vector.VectorTileBuilder
import kpn.server.json.Json
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

import java.util.concurrent.Executor
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy

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
  def analysisExecutor(@Value("${app.analyzer-thread-pool-size:4}") poolSize: Int): Executor = {
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
  def analyzerHistory(@Value("${app.analyzer-history:false}") value: Boolean): Boolean = {
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
  def rasterTileRepository: TileFileRepository = {
    new TileFileRepositoryImpl("/kpn/tiles", "png")
  }

  @Bean
  def vectorTileRepository: TileFileRepository = {
    new TileFileRepositoryImpl("/kpn/tiles", "mvt")
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
