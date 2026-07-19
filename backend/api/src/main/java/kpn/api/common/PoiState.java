package kpn.api.common;

import kpn.api.custom.Timestamp;

import java.util.Optional;

public record PoiState(
  String _id,
  Optional<String> imageLink,
  Optional<String> imageStatus,
  Optional<String> imageStatusDetail,
  Optional<Timestamp> imageFirstSeen,
  Optional<Timestamp> imageLastSeen
) {
}

/*
package kpn.api.common

import kpn.api.custom.Timestamp
import kpn.core.doc.WithStringId

case class PoiState(
  _id: String,
  imageLink: Option[String] = None,
  imageStatus: Option[String] = None,
  imageStatusDetail: Option[String] = None,
  imageFirstSeen: Option[Timestamp] = None,
  imageLastSeen: Option[Timestamp] = None,
) extends WithStringId

*/
