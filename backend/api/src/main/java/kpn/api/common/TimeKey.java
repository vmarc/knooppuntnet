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
