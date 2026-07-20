package kpn.api.common.monitor;

import kpn.api.common.monitor.MonitorCommand;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record MonitorMessage(
  ImmutableList<MonitorCommand> commands,
  ImmutableList<String> errors,
  Optional<String> exception
) {
}

/* TODO migrate
package kpn.api.common.monitor

import kpn.core.doc.Storable

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

*/
