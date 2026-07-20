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
import kpn.core.doc.WithStringId;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

/*
  Describes the changes made to a given network node in a given changeset.
*/
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
  ImmutableList<Ref> addedToNetwork, // added to network relation (not included when only added to route within network)
  ImmutableList<Ref> removedFromNetwork, // removed from network relation (not included when only removed to route within network)
  Optional<FactDiffs> factDiffs,
  ImmutableList<Fact> facts,
  ImmutableList<Tag> initialTags,
  Optional<LatLonImpl> initialLatLon,
  // following values are filled in by NodeChangeAnalyzer.analyzed
  Boolean happy,
  Boolean investigate,
  Boolean impact,
  Boolean locationHappy,
  Boolean locationInvestigate,
  Boolean locationImpact,
  Optional<String> comment
) implements WithStringId {
  public Long id() {
    return key.elementId();
  }
  public boolean isEmpty() {
    return connectionChanges.isEmpty() &&
      roleConnectionChanges.isEmpty() &&
      definedInNetworkChanges.isEmpty() &&
      tagDiffs.isEmpty() &&
      nodeMoved.isEmpty() &&
      addedToRoute.isEmpty() &&
      removedFromRoute.isEmpty() &&
      addedToNetwork.isEmpty() &&
      removedFromNetwork.isEmpty() &&
      factDiffs.isEmpty() &&
      facts.isEmpty();
  }

  public Ref toRef() {
    return new Ref(id(), name().orElse(id().toString()));
  }
}
