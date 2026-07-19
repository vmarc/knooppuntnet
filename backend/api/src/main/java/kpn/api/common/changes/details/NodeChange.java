package kpn.api.common.changes.details;

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
import kpn.api.custom.Subset;
import kpn.api.custom.Tag;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record NodeChange(
  String _id,
  ChangeKey key,
  ChangeType changeType,
  ImmutableList<Subset> subsets,
  ImmutableList<String> locations,
  Optional<String> name,
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
  Boolean investigate,
  Boolean impact,
  Boolean locationHappy,
  Boolean locationInvestigate,
  Boolean locationImpact,
  Optional<String> comment
) {
}

/*
package kpn.api.common.changes.details

import kpn.api.common.ChangeType
import kpn.api.common.Fact
import kpn.api.common.LatLonImpl
import kpn.api.common.common.Ref
import kpn.api.common.data.MetaData
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.node.NodeMoved
import kpn.api.custom.Subset
import kpn.api.custom.Tag
import kpn.core.doc.WithStringId

*** 
  Describes the changes made to a given network node in a given changeset.
 *** 
case class NodeChange(
  _id: String,
  key: ChangeKey,
  changeType: ChangeType,
  subsets: Seq[Subset],
  locations: Seq[String],
  name: Option[String],
  before: Option[MetaData],
  after: Option[MetaData],
  connectionChanges: Seq[RefBooleanChange],
  roleConnectionChanges: Seq[RefBooleanChange],
  definedInNetworkChanges: Seq[RefBooleanChange],
  tagDiffs: Option[TagDiffs],
  nodeMoved: Option[NodeMoved],
  addedToRoute: Seq[Ref],
  removedFromRoute: Seq[Ref],
  addedToNetwork: Seq[Ref], // added to network relation (not included when only added to route within network)
  removedFromNetwork: Seq[Ref], // removed from network relation (not included when only removed to route within network)
  factDiffs: Option[FactDiffs],
  facts: Seq[Fact],
  initialTags: Option[Seq[Tag]],
  initialLatLon: Option[LatLonImpl],
  // following values are filled in by NodeChangeAnalyzer.analyzed
  happy: Boolean = false,
  investigate: Boolean = false,
  impact: Boolean = false,
  locationHappy: Boolean = false,
  locationInvestigate: Boolean = false,
  locationImpact: Boolean = false,
  comment: Option[String] = None
) extends WithStringId {

  def id: Long = key.elementId

  def isEmpty: Boolean = {
    connectionChanges.isEmpty &&
      roleConnectionChanges.isEmpty &&
      definedInNetworkChanges.isEmpty &&
      tagDiffs.isEmpty &&
      nodeMoved.isEmpty &&
      addedToRoute.isEmpty &&
      removedFromRoute.isEmpty &&
      addedToNetwork.isEmpty &&
      removedFromNetwork.isEmpty &&
      factDiffs.isEmpty &&
      facts.isEmpty
  }

  def toRef: Ref = Ref(id, name.getOrElse(s"id"))
}

*/
