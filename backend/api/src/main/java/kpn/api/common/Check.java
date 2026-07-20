package kpn.api.common;

public record Check(
  Long nodeId,
  String nodeName,
  Long expected,
  Long actual
) {}
