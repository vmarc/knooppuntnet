package kpn.server.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.WebSocketHandler


@Configuration
@EnableWebSocket
class WebSocketConfig extends WebSocketConfigurer {
  def registerWebSocketHandlers(registry: WebSocketHandlerRegistry): Unit = {
    registry.addHandler(webSocketHandler, "/websocket")
  }

  @Bean
  def webSocketHandler: WebSocketHandler = new ServerWebSocketHandler()
}