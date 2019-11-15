package kpn.api.common.data

import kpn.api.common.LatLon
import kpn.api.common.data.raw.RawNode
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp

case class Node(raw: RawNode) extends Element with LatLon {

  def id: Long = raw.id

  def latitude: String = raw.latitude

  def longitude: String = raw.longitude

  def version: Long = raw.version

  def timestamp: Timestamp = raw.timestamp

  def changeSetId: Long = raw.changeSetId

  def tags: Tags = raw.tags

  override def isNode: Boolean = true

}
