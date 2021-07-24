package kpn.api.common.diff

import kpn.api.common.common.ToStringBuilder

case class NetworkDataUpdate(
  before: NetworkData,
  after: NetworkData
) {
  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("before", before).
    field("after", after).
    build
}
