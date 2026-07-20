package kpn.api.common;

import kpn.api.common.ChangeSetElementRefs;
import kpn.api.custom.Subset;

public record ChangeSetSubsetElementRefs(
  Subset subset,
  ChangeSetElementRefs elementRefs
) {}

/* TODO migrate

  def elementIds: Seq[Long] = elementRefs.elementIds

  def happy: Boolean = elementRefs.happy

  def investigate: Boolean = elementRefs.investigate

*/
