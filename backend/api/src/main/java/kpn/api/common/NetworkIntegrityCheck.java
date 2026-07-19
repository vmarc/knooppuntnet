package kpn.api.common;

public record NetworkIntegrityCheck(
  Long count,
  Long failed
) {
}

/*
package kpn.api.common

case class NetworkIntegrityCheck(count: Long, failed: Long)

*/
