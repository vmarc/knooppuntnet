package kpn.server.monitor.route

import kpn.api.common.monitor.MonitorRouteUpdateStatus
import kpn.server.json.Json
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

class MonitorUpdateReporterWebsocket(session: WebSocketSession) extends MonitorUpdateReporter {
  override def report(status: MonitorRouteUpdateStatus): Unit = {
    val payload = Json.string(status)
    session.sendMessage(new TextMessage(payload))
  }
}
