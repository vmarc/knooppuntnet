package kpn.api.common.location;

import com.google.common.collect.ImmutableList;

public record Location(
  ImmutableList<String> names
) {
}

/*
package kpn.api.common.location

object Location {
  def empty: Location = {
    Location(Seq.empty)
  }
}

case class Location(names: Seq[String])

*/
