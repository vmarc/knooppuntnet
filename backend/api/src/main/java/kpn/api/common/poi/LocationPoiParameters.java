package kpn.api.common.poi;

public record LocationPoiParameters(
  Long pageSize,
  Long pageIndex
) {
}

/*
package kpn.api.common.poi

case class LocationPoiParameters(
  pageSize: Long = 5,
  pageIndex: Long = 0
)

*/
