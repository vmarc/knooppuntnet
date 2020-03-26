package kpn.server.api.status

trait SystemStatusMonitor {
  def snapshot(): Unit
}
