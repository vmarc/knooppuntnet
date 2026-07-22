package kpn.core.doc

import kpn.api.common.Fact
import kpn.api.common.data.Tagable
import kpn.api.custom.Tag

case class BaseNodeDoc(
  _id: Long,
  active: Boolean,
  base: NodeBaseData,
  facts: Seq[Fact],
  tiles: Seq[String],
) extends Tagable with WithId {

  def tags: Seq[Tag] = {
    base.raw.tags
  }

  def deactivated: BaseNodeDoc = {
    copy(active = false)
  }
}
