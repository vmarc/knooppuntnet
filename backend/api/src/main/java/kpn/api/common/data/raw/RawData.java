package kpn.api.common.data.raw;

import kpn.api.common.data.raw.RawNode;
import kpn.api.common.data.raw.RawRelation;
import kpn.api.common.data.raw.RawWay;
import kpn.api.custom.Timestamp;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record RawData(
  Optional<Timestamp> timestamp,
  ImmutableList<RawNode> nodes,
  ImmutableList<RawWay> ways,
  ImmutableList<RawRelation> relations
) {
}

/* TODO migrate
package kpn.api.common.data.raw

import kpn.api.custom.Timestamp

object RawData {
  def merge(datas: RawData*): RawData = {
    if (datas.size == 1) {
      datas.head
    }
    else {
      val nodes = datas.flatMap(_.nodes).distinct
      val ways = datas.flatMap(_.ways).distinct
      val relations = datas.flatMap(_.relations).distinct
      RawData(datas.head.timestamp, nodes, ways, relations)
    }
  }

}

case class RawData(
  timestamp: Option[Timestamp] = None,
  nodes: Seq[RawNode] = Seq.empty,
  ways: Seq[RawWay] = Seq.empty,
  relations: Seq[RawRelation] = Seq.empty
) {

  def nodeWithId(id: Long): Option[RawNode] = nodes.find(_.id == id)

  def wayWithId(id: Long): Option[RawWay] = ways.find(_.id == id)

  def relationWithId(id: Long): Option[RawRelation] = relations.find(_.id == id)

  def isEmpty: Boolean = nodes.isEmpty && ways.isEmpty && relations.isEmpty

}

*/
