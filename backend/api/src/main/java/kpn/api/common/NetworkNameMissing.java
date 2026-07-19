package kpn.api.common;

import java.util.Optional;

public record NetworkNameMissing(
  Optional<String> dummy
) {
}

/*
package kpn.api.common

import kpn.core.doc.Storable

case class NetworkNameMissing(dummy: Option[String] = None) extends Storable

*/
