package kpn.api.common.changes.details;

import kpn.api.common.common.Ref;

public record RefBooleanChange(
  Ref ref,
  Boolean after
) {
}

/*
package kpn.api.common.changes.details

import kpn.api.common.common.Ref

case class RefBooleanChange(ref: Ref, after: Boolean)

*/
