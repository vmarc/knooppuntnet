package kpn.api.common.data

trait Member {

  def role: Option[String]

  def isNode: Boolean = false

  def isWay: Boolean = false

  def isRelation: Boolean = false

}
