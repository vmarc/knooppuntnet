package kpn.shared.data.raw

case class RawMember(memberType: String, ref: Long, role: Option[String]) {

  def isNode: Boolean = memberType == "node"

  def isWay: Boolean = memberType == "way"

  def isRelation: Boolean = memberType == "relation"
}
