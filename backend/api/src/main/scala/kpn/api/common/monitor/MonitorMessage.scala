package kpn.api.common.monitor

import kpn.api.id.Storable

object MonitorMessage {
  def apply(commands: MonitorCommand*): MonitorMessage = {
    MonitorMessage(commands = commands)
  }
}

case class MonitorMessage(
  commands: Seq[MonitorCommand] = Seq.empty,
  errors: Option[Seq[String]] = None,
  exception: Option[String] = None
) extends Storable
