package kpn.api.common.node;

public record NodeNetworkIntegrityCheck(
  Boolean failed,
  Long expected,
  Long actual
) {}
