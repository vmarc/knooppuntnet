package kpn.api.common;

public record Check(
  Long nodeId,
  String nodeName,
  Long expected,
  Long actual
) {
}

/*
package kpn.api.common

case class Check(
  nodeId: Long,
  nodeName: String,
  expected: Long,
  actual: Long
)

*/
