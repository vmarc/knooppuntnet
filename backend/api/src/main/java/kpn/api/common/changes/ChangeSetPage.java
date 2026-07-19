package kpn.api.common.changes;

import kpn.api.common.changes.ChangeSetDetail;

import com.google.common.collect.ImmutableList;

public record ChangeSetPage(
  ImmutableList<ChangeSetDetail> details
) {
}

/*
package kpn.api.common.changes

case class ChangeSetPage(
  details: Seq[ChangeSetDetail]
)

*/
