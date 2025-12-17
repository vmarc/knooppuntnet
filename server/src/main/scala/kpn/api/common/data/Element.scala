package kpn.api.common.data

import kpn.api.common.data.raw.Raw
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp

trait Element extends Meta with Tagable {

  def id: Long

  def version: Long

  def timestamp: Timestamp

  def changeSetId: Long

  def tags: Seq[Tag]

  def isNode: Boolean = false

  def isWay: Boolean = false

  def isRelation: Boolean = false

  def toMeta: MetaData = MetaData(version, timestamp, changeSetId)

  def raw: Raw = {
    Raw(
      version,
      changeSetId,
      timestamp,
      tags
    )
  }
}
