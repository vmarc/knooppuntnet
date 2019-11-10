package kpn.api.common.data

import kpn.api.custom.Tags
import kpn.api.custom.Timestamp

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
