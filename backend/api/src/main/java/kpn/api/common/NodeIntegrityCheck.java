package kpn.api.common;

public record NodeIntegrityCheck(
  String nodeName,
  Long nodeId,
  Long actual,
  Long expected,
  Boolean failed
) {
}
