package kpn.api.common.diff;

import kpn.api.common.diff.NetworkData;

import java.util.Optional;

public record NetworkDataUpdate(
  Optional<NetworkData> before,
  Optional<NetworkData> after
) {
}

/*
package kpn.api.common.diff

case class NetworkDataUpdate(
  before: Option[NetworkData],
  after: Option[NetworkData]
)

*/
