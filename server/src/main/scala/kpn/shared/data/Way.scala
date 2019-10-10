package kpn.shared.data

import kpn.shared.data.raw.RawWay
import kpn.shared.Timestamp

case class Way(raw: RawWay, nodes: Seq[Node], length: Int) extends Element {

  def id: Long = raw.id

  def version: Int = raw.version

  def timestamp: Timestamp = raw.timestamp

  def changeSetId: Long = raw.changeSetId

  def tags: Tags = raw.tags

  override def isWay: Boolean = true

}
