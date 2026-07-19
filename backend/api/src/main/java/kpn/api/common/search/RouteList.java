package kpn.api.common.search;

import kpn.api.common.search.RouteListItem;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record RouteList(
  ImmutableList<RouteListItem> international,
  ImmutableList<RouteListItem> national,
  ImmutableList<RouteListItem> regional,
  ImmutableList<RouteListItem> local,
  ImmutableList<RouteListItem> unknown,
  Long size
) {
}

/*
package kpn.api.common.search

case class RouteList(
  international: Option[Seq[RouteListItem]],
  national: Option[Seq[RouteListItem]],
  regional: Option[Seq[RouteListItem]],
  local: Option[Seq[RouteListItem]],
  unknown: Option[Seq[RouteListItem]],
  size: Long,
)

*/
