package kpn.api.common;

public record NodeIntegrityCheck(
  String nodeName,
  Long nodeId,
  Long actual,
  Long expected,
  Boolean failed
) {
}

/*
package kpn.api.common

case class NodeIntegrityCheck(nodeName: String, nodeId: Long, actual: Long, expected: Long, failed: Boolean)

*/
