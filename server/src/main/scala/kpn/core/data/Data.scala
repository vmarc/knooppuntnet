package kpn.core.data

import kpn.shared.Timestamp
import kpn.shared.data.Node
import kpn.shared.data.Relation
import kpn.shared.data.Way
import kpn.shared.data.raw.RawData

case class Data(
  raw: RawData,
  nodes: Map[Long, Node],
  ways: Map[Long, Way],
  relations: Map[Long, Relation]
) {

  def timestamp: Option[Timestamp] = raw.timestamp

}
