package kpn.core.data

import kpn.api.common.data.Node
import kpn.api.common.data.Way
import kpn.api.common.data.raw.RawData
import kpn.api.custom.Relation
import kpn.api.custom.Timestamp

object Data {
  def empty: Data = {
    Data(
      RawData(),
      Map.empty,
      Map.empty,
      Map.empty
    )
  }
}

case class Data(
  raw: RawData,
  nodes: Map[Long, Node],
  ways: Map[Long, Way],
  relations: Map[Long, Relation]
) {

  def timestamp: Option[Timestamp] = raw.timestamp

}
