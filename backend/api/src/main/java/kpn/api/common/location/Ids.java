package kpn.api.common.location;

import com.google.common.collect.ImmutableList;

public record Ids(
  ImmutableList<Long> ids
) {
}

/*
package kpn.api.common.location

case class Ids(ids: Seq[Long])

*/
