package kpn.server.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean

@Configuration
@EnableWebSocket
@Profile(Array("web"))
class WebSocketConfig(webSocketHandler: ServerWebSocketHandler) extends WebSocketConfigurer {

  def registerWebSocketHandlers(registry: WebSocketHandlerRegistry): Unit = {
    registry.addHandler(webSocketHandler, "/websocket").setAllowedOrigins(
      "https://knooppuntnet.nl",
      "https://knooppuntnet.be",
      "https://experimental.knooppuntnet.nl",
      "https://experimental.knooppuntnet.be",
    )
  }

  @Bean
  def createServletServerContainerFactoryBean: ServletServerContainerFactoryBean = {
    val container = new ServletServerContainerFactoryBean
    container.setMaxTextMessageBufferSize(21 * 1024 * 1024)
    container
  }
}
