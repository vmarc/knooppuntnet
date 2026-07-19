package kpn.api.common;

public record TimeKey(
  Long year,
  Long month,
  Long day,
  Long hour,
  Long minute,
  Long second
) {
}

/*
package kpn.api.common

case class TimeKey(year: Long, month: Long, day: Long, hour: Long, minute: Long, second: Long)

*/
