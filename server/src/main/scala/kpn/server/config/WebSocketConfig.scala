package kpn.server.config

import kpn.core.util.Log
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean

@Configuration
@EnableWebSocket
class WebSocketConfig(webSocketHandler: ServerWebSocketHandler) extends WebSocketConfigurer {
  private val log = Log(classOf[WebSocketConfig])

  def registerWebSocketHandlers(registry: WebSocketHandlerRegistry): Unit = {
    registry.addHandler(webSocketHandler, "/websocket").setAllowedOriginPatterns("*");
  }

  @Bean
  def createServletServerContainerFactoryBean: ServletServerContainerFactoryBean = {
    val container = new ServletServerContainerFactoryBean
    container.setMaxTextMessageBufferSize(20 * 1024 * 1024)
    container
  }
}