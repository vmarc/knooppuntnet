package kpn.api.common.data

import kpn.api.common.data.raw.RawMember

trait Member {

  def role: Option[String]

  def isNode: Boolean = false

  def isWay: Boolean = false

  def isRelation: Boolean = false

  def toRaw: RawMember
}
