package kpn.api.common.data;

import kpn.api.common.data.Node;
import kpn.api.custom.Tag;
import kpn.api.custom.Timestamp;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList;

public record Way(
  Long id,
  Long version,
  Timestamp timestamp,
  Long changeSetId,
  ImmutableList<Tag> tags,
  ImmutableList<Node> nodes,
  Long length
) {
}

/*
package kpn.api.common.data

import kpn.api.common.data.raw.RawWay
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp

case class Way(
  id: Long,
  version: Long,
  timestamp: Timestamp,
  changeSetId: Long,
  tags: Seq[Tag],
  nodes: Vector[Node],
  length: Long // meters
) extends Element {
  override def isWay: Boolean = true

  def nodeIds: Seq[Long] = {
    nodes.map(_.id)
  }

  def toRaw: RawWay = {
    RawWay(
      id,
      version,
      timestamp,
      changeSetId,
      nodes.map(_.id),
      tags
    )
  }
}

*/
