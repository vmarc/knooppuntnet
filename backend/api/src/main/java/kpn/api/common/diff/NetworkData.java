package kpn.api.common.diff;

import kpn.api.common.data.MetaData;

import java.util.Optional;

public record NetworkData(
  MetaData metaData,
  Optional<String> name
) {
}

/*
package kpn.api.common.diff

import kpn.api.common.data.MetaData

case class NetworkData(
  metaData: MetaData,
  name: Option[String]
)

*/
