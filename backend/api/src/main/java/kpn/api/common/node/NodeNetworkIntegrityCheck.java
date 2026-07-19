package kpn.api.common.node;

public record NodeNetworkIntegrityCheck(
  Boolean failed,
  Long expected,
  Long actual
) {
}

/*
package kpn.api.common.node

case class NodeNetworkIntegrityCheck(failed: Boolean, expected: Long, actual: Long)

*/
