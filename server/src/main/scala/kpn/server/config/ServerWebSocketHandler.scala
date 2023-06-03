package kpn.server.config

import kpn.core.util.Log
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.AbstractWebSocketHandler

class ServerWebSocketHandler extends AbstractWebSocketHandler {

  private val log = Log(classOf[ServerWebSocketHandler])

  override def handleTextMessage(session: WebSocketSession, message: TextMessage): Unit = {
    val request = message.getPayload
    log.info(s"handleTextMessage() received: '${request.substring(0, 24)}'")
    session.sendMessage(new TextMessage("{message: 'one'}"))
    session.sendMessage(new TextMessage("{message: 'two'}"))
    session.sendMessage(new TextMessage("{message: 'three'}"))
    //session.close()
  }

  override def handleTransportError(session: WebSocketSession, exception: Throwable): Unit = {
    val user = if (session != null && session.getPrincipal != null) {
      session.getPrincipal.getName
    }
    else {
      "unknown"
    }
    log.warn(s"user=$user, transport error: ${exception.toString}");
    super.handleTransportError(session, exception);
  }

  override def afterConnectionClosed(session: WebSocketSession, status: CloseStatus): Unit = {
    if (status != CloseStatus.NORMAL) {
      val user = if (session != null && session.getPrincipal != null) {
        session.getPrincipal.getName
      }
      else {
        "unknown"
      }
      log.warn(s"Error in websocket connection: user=$user, code=${status.getCode}, reason=${status.getReason}");
    }
    super.afterConnectionClosed(session, status);
  }
}
