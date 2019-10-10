package kpn.shared.data

import kpn.shared.LatLon
import kpn.shared.data.raw.RawNode
import kpn.shared.Timestamp

case class Node(raw: RawNode) extends Element with LatLon {

  def id: Long = raw.id

  def latitude: String = raw.latitude

  def longitude: String = raw.longitude

  def version: Int = raw.version

  def timestamp: Timestamp = raw.timestamp

  def changeSetId: Long = raw.changeSetId

  def tags: Tags = raw.tags

  override def isNode: Boolean = true

}
