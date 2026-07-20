package kpn.api.common.monitor;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record MonitorRouteSaveResult(
  Boolean analyzed,
  ImmutableList<String> errors,
  Optional<String> exception
) {}

/* TODO migrate
package kpn.api.common.monitor

case class MonitorRouteSaveResult(
  analyzed: Boolean = false,
  errors: Seq[String] = Seq.empty,
  exception: Option[String] = None
)

*/
