package kpn.server.config

import akka.actor.ActorSystem
import com.fasterxml.jackson.databind.ObjectMapper
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import kpn.core.app.ActorSystemConfig
import kpn.server.analyzer.engine.tiles.TileBuilder
import kpn.server.analyzer.engine.tiles.TileFileRepository
import kpn.server.analyzer.engine.tiles.TileFileRepositoryImpl
import kpn.server.analyzer.engine.tiles.raster.RasterTileBuilder
import kpn.server.analyzer.engine.tiles.vector.VectorTileBuilder
import kpn.server.json.Json
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class ServerConfiguration {

  @Bean
  @Primary
  def objectMapper: ObjectMapper = Json.objectMapper

  @Bean
  def threadMetrics = new JvmThreadMetrics

  @Bean
  def system: ActorSystem = ActorSystemConfig.actorSystem()

  @Bean
  def graphLoadEnabled(@Value("${app.graph-load-enabled:false}") value: Boolean): Boolean = {
    value
  }

  @Bean
  def rasterTileBuilder: TileBuilder = {
    new RasterTileBuilder()
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

}
