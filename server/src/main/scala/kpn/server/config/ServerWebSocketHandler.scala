package kpn.server.config

import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.core.util.Log
import kpn.server.json.Json
import kpn.server.monitor.route.update.MonitorRouteUpdateExecutor
import kpn.server.monitor.route.update.MonitorUpdateContext
import kpn.server.monitor.route.update.MonitorUpdateReporterWebsocket
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.AbstractWebSocketHandler

@Component
class ServerWebSocketHandler(
  applicationContext: ApplicationContext
) extends AbstractWebSocketHandler {

  private val log = Log(classOf[ServerWebSocketHandler])

  override def handleTextMessage(session: WebSocketSession, message: TextMessage): Unit = {
    val user = if (session != null && session.getPrincipal != null) {
      session.getPrincipal.getName
    }
    else {
      "unknown"
    }
    val payload = message.getPayload
    val command = Json.value(payload, classOf[MonitorRouteUpdate])
    val reporter = new MonitorUpdateReporterWebsocket(session)
    Log.context(Seq("route-update", s"group=${command.groupName}", s"route=${command.routeName}")) {
      val printableCommand = command.copy(
        referenceGpx = command.referenceGpx.map(gpx => gpx.substring(0, 25))
      )
      log.info("" + printableCommand)
      val context = MonitorUpdateContext(
        user,
        reporter,
        command,
      )
      try {
        applicationContext.getBean(classOf[MonitorRouteUpdateExecutor]).execute(context)
      }
      finally {
        session.close()
      }
    }
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
