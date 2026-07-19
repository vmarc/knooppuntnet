package kpn.api.common.node;

import kpn.api.common.ChangeType;
import kpn.api.common.Fact;
import kpn.api.common.LatLonImpl;
import kpn.api.common.changes.details.ChangeKey;
import kpn.api.common.changes.details.RefBooleanChange;
import kpn.api.common.common.Ref;
import kpn.api.common.data.MetaData;
import kpn.api.common.diff.TagDiffs;
import kpn.api.common.diff.common.FactDiffs;
import kpn.api.common.diff.node.NodeMoved;
import kpn.api.custom.Tag;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record NodeChangeInfo(
  Long rowIndex,
  Long id,
  Optional<Long> version,
  ChangeKey changeKey,
  ChangeType changeType,
  ImmutableList<Tag> changeTags,
  Optional<String> comment,
  Optional<MetaData> before,
  Optional<MetaData> after,
  ImmutableList<RefBooleanChange> connectionChanges,
  ImmutableList<RefBooleanChange> roleConnectionChanges,
  ImmutableList<RefBooleanChange> definedInNetworkChanges,
  Optional<TagDiffs> tagDiffs,
  Optional<NodeMoved> nodeMoved,
  ImmutableList<Ref> addedToRoute,
  ImmutableList<Ref> removedFromRoute,
  ImmutableList<Ref> addedToNetwork,
  ImmutableList<Ref> removedFromNetwork,
  Optional<FactDiffs> factDiffs,
  ImmutableList<Fact> facts,
  ImmutableList<Tag> initialTags,
  Optional<LatLonImpl> initialLatLon,
  Boolean happy,
  Boolean investigate
) {
}

/*
package kpn.api.common.node

import kpn.api.common.ChangeType
import kpn.api.common.Fact
import kpn.api.common.LatLonImpl
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.changes.details.RefBooleanChange
import kpn.api.common.common.Ref
import kpn.api.common.data.MetaData
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.node.NodeMoved
import kpn.api.custom.Tag

case class NodeChangeInfo(
  rowIndex: Long,
  id: Long,
  version: Option[Long],
  changeKey: ChangeKey,
  changeType: ChangeType,
  changeTags: Seq[Tag],
  comment: Option[String],
  before: Option[MetaData],
  after: Option[MetaData],
  connectionChanges: Seq[RefBooleanChange],
  roleConnectionChanges: Seq[RefBooleanChange],
  definedInNetworkChanges: Seq[RefBooleanChange],
  tagDiffs: Option[TagDiffs],
  nodeMoved: Option[NodeMoved],
  addedToRoute: Seq[Ref],
  removedFromRoute: Seq[Ref],
  addedToNetwork: Seq[Ref],
  removedFromNetwork: Seq[Ref],
  factDiffs: Option[FactDiffs],
  facts: Seq[Fact],
  initialTags: Option[Seq[Tag]],
  initialLatLon: Option[LatLonImpl],
  happy: Boolean,
  investigate: Boolean
)

*/
