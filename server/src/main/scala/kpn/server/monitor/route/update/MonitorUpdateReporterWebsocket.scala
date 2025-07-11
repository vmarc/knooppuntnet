package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorMessage
import kpn.server.json.Json
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

class MonitorUpdateReporterWebsocket(session: WebSocketSession) extends MonitorUpdateReporter {
  override def report(message: MonitorMessage): Unit = {
    val payload = Json.string(message)
    session.sendMessage(new TextMessage(payload))
  }
}
