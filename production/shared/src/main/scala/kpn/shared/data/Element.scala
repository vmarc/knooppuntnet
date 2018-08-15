package kpn.shared.data

import kpn.shared.Timestamp

trait Element extends Meta with Tagable {

  def id: Long
  def version: Int
  def timestamp: Timestamp
  def changeSetId: Long
  def tags: Tags

  def isNode: Boolean = false
  def isWay: Boolean = false
  def isRelation: Boolean = false

  def toMeta: MetaData = MetaData(version, timestamp, changeSetId)

}
