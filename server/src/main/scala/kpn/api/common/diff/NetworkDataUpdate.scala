package kpn.api.common.diff

import kpn.api.common.common.ToStringBuilder

case class NetworkDataUpdate(
  before: NetworkData,
  after: NetworkData
) {

  def happy: Boolean = false // diffs.happy

  def isNewVersion: Boolean = before.relation.version != after.relation.version

  def investigate: Boolean =  false // diffs.investigate

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("before", before).
    field("after", after).
    build
}
