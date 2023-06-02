package kpn.server.config

import kpn.core.util.Log
import org.springframework.web.socket.handler.TextWebSocketHandler
import org.springframework.web.socket.BinaryMessage
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.PongMessage
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession

class ServerWebSocketHandler extends TextWebSocketHandler {

  private val log = Log(classOf[ServerWebSocketHandler])

  override def handleTextMessage(session: WebSocketSession, message: TextMessage): Unit = {
    val request = message.getPayload
    log.info(s"handleTextMessage() received: '$request'")
    session.sendMessage(new TextMessage("{message: 'one'}"))
    session.sendMessage(new TextMessage("{message: 'two'}"))
    session.sendMessage(new TextMessage("{message: 'three'}"))
    session.close()
  }

  override def afterConnectionEstablished(session: WebSocketSession): Unit = {
    super.afterConnectionEstablished(session);
    log.info("afterConnectionEstablished()")
  }

  @throws[Exception]
  override def handleMessage(session: WebSocketSession, message: WebSocketMessage[_]): Unit = {
    log.info("handleMessage()")
    super.handleMessage(session, message);
  }

  override def handleBinaryMessage(session: WebSocketSession, message: BinaryMessage): Unit = {
    log.info("handleBinaryMessage()");
    super.handleBinaryMessage(session, message);
  }

  override def handlePongMessage(session: WebSocketSession, message: PongMessage): Unit = {
    log.info("handlePongMessage()");
    super.handlePongMessage(session, message);
  }

  override def handleTransportError(session: WebSocketSession, exception: Throwable): Unit = {
    log.info("handleTransportError()");
    super.handleTransportError(session, exception);
  }

  override def afterConnectionClosed(session: WebSocketSession, status: CloseStatus): Unit = {
    super.afterConnectionClosed(session, status);
    log.info("afterConnectionClosed()");
  }
}
