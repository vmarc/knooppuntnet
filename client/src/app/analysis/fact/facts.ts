import {List, Map} from "immutable";
import {FactLevel} from "./fact-level";

class FactDefinition {
  constructor(public name: string, public level: FactLevel) {
  }
}

export class Facts {

  static readonly facts = List<FactDefinition>([
    new FactDefinition("RouteNotContinious", FactLevel.error),
    new FactDefinition("RouteUnusedSegments", FactLevel.error),
    new FactDefinition("RouteNodeMissingInWays", FactLevel.error),
    new FactDefinition("RouteRedundantNodes", FactLevel.error),
    new FactDefinition("RouteWithoutWays", FactLevel.error),
    new FactDefinition("RouteFixmetodo", FactLevel.error),
    new FactDefinition("RouteNameMissing", FactLevel.error),
    new FactDefinition("RouteEndNodeMismatch", FactLevel.error),
    new FactDefinition("RouteStartNodeMismatch", FactLevel.error),
    new FactDefinition("RouteTagMissing", FactLevel.error),
    new FactDefinition("RouteTagInvalid", FactLevel.error),
    new FactDefinition("RouteUnexpectedNode", FactLevel.error),
    new FactDefinition("RouteUnexpectedRelation", FactLevel.error),
    new FactDefinition("OrphanRoute", FactLevel.error),
    new FactDefinition("OrphanNode", FactLevel.error),
    new FactDefinition("RouteIncomplete", FactLevel.info),
    new FactDefinition("RouteIncompleteOk", FactLevel.info),
    new FactDefinition("RouteUnaccessible", FactLevel.info),
    new FactDefinition("RouteInvalidSortingOrder", FactLevel.info),
    new FactDefinition("RouteReversed", FactLevel.info),
    new FactDefinition("RouteNodeNameMismatch", FactLevel.info),
    new FactDefinition("RouteNotForward", FactLevel.error),
    new FactDefinition("RouteNotBackward", FactLevel.error),
    new FactDefinition("RouteAnalysisFailed", FactLevel.error),
    new FactDefinition("RouteOverlappingWays", FactLevel.error),
    new FactDefinition("RouteSuspiciousWays", FactLevel.error),
    new FactDefinition("RouteBroken", FactLevel.other),
    new FactDefinition("RouteOneWay", FactLevel.info),
    new FactDefinition("RouteNotOneWay", FactLevel.info),
    new FactDefinition("NameMissing", FactLevel.error),
    new FactDefinition("IgnoreForeignCountry", FactLevel.other),
    new FactDefinition("IgnoreNoNodeNetwork", FactLevel.other),
    new FactDefinition("IgnoreNoNetworkNodes", FactLevel.error),
    new FactDefinition("IgnoreNetworkTaggedAsRoute", FactLevel.other),
    new FactDefinition("IgnoreUnsupportedSubset", FactLevel.other),
    new FactDefinition("Added", FactLevel.other),
    new FactDefinition("BecomeIgnored", FactLevel.other),
    new FactDefinition("BecomeOrphan", FactLevel.other),
    new FactDefinition("Deleted", FactLevel.other),
    new FactDefinition("IgnoreNetworkCollection", FactLevel.other),
    new FactDefinition("IgnoreTagBased", FactLevel.other),
    new FactDefinition("IntegrityCheck", FactLevel.other),
    new FactDefinition("LostBicycleNodeTag", FactLevel.other),
    new FactDefinition("LostHikingNodeTag", FactLevel.other),
    new FactDefinition("LostHorseNodeTag", FactLevel.other),
    new FactDefinition("LostMotorboatNodeTag", FactLevel.other),
    new FactDefinition("LostCanoeNodeTag", FactLevel.other),
    new FactDefinition("LostInlineSkateNodeTag", FactLevel.other),
    new FactDefinition("WasIgnored", FactLevel.error),
    new FactDefinition("WasOrphan", FactLevel.other)
  ]);

  private static readonly keyValues: List<[string,string]> = Facts.facts.map(f => {
    const row: [string, string] = [f.name, f.level];
    return row;
  });

  static readonly factLevels: Map<string, string> = Map(Facts.keyValues);

  static readonly allFactNames = Facts.facts.map(f => f.name);

}
