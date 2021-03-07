package kpn.api.common.diff

import kpn.api.common.common.ToStringBuilder

case class TagDetail(action: TagDetailType, key: String, valueBefore: Option[String], valueAfter: Option[String]) {

  def name: String = action.toString.toLowerCase

  def sortKey: String = action.name + " " + key

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("action", action).
    field("key", key).
    field("valueBefore", valueBefore).
    field("valueAfter", valueAfter).
    build
}
