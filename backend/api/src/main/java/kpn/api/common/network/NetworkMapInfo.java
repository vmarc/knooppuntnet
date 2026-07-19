package kpn.api.common.network;

import kpn.api.common.network.NetworkShape;

public record NetworkMapInfo(
  Long id,
  String name,
  NetworkShape map
) {
}

/*
package kpn.api.common.network

case class NetworkMapInfo(id: Long, name: String, map: NetworkShape)

*/
