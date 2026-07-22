package kpn.api.common.data;

import kpn.api.custom.Tag;
import kpn.api.custom.Timestamp;

import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record Way(
  @NonNull Long id,
  @NonNull Long version,
  @NonNull Timestamp timestamp,
  @NonNull Long changeSetId,
  @NonNull ImmutableList<Tag> tags,
  @NonNull ImmutableList<Node> nodes,
  @NonNull Long length
) implements Element {}

/* TODO migrate
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
