package kpn.api.common;

import kpn.core.doc.Storable;

import java.util.Optional;

public record NetworkNameMissing(
  Optional<String> dummy
) implements Storable {
}

/* TODO migrate

case class NetworkNameMissing(dummy: Option[String] = None) extends Storable

*/
