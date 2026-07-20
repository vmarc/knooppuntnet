package kpn.api.common.poi;

public record LocationPoiParameters(
  Long pageSize,
  Long pageIndex
) {
}

/* TODO migrate

case class LocationPoiParameters(
  pageSize: Long = 5,
  pageIndex: Long = 0
)

*/
