package kpn.core.tools.next.domain

trait OldMember {

  def role: Option[String]

  def isNode: Boolean = false

  def isWay: Boolean = false

  def isRelation: Boolean = false
}
