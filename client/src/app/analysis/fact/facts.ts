import {List, Map} from "immutable";

class FactDefinition {
  constructor(public name: string, public level: string) {
  }
}

export class Facts {

  static readonly facts = List<FactDefinition>([
    new FactDefinition("RouteNotContinious", "error"),
    new FactDefinition("RouteUnusedSegments", "error"),
    new FactDefinition("RouteNodeMissingInWays", "error"),
    new FactDefinition("RouteRedundantNodes", "error"),
    new FactDefinition("RouteWithoutWays", "error"),
    new FactDefinition("RouteFixmetodo", "error"),
    new FactDefinition("RouteNameMissing", "error"),
    new FactDefinition("RouteEndNodeMismatch", "error"),
    new FactDefinition("RouteStartNodeMismatch", "error"),
    new FactDefinition("RouteTagMissing", "error"),
    new FactDefinition("RouteTagInvalid", "error"),
    new FactDefinition("RouteUnexpectedNode", "error"),
    new FactDefinition("RouteUnexpectedRelation", "error"),
    new FactDefinition("OrphanRoute", "error"),
    new FactDefinition("OrphanNode", "error"),
    new FactDefinition("RouteIncomplete", "info"),
    new FactDefinition("RouteIncompleteOk", "info"),
    new FactDefinition("RouteUnaccessible", "info"),
    new FactDefinition("RouteInvalidSortingOrder", "info"),
    new FactDefinition("RouteReversed", "info"),
    new FactDefinition("RouteNodeNameMismatch", "info"),
    new FactDefinition("RouteNotForward", "error"),
    new FactDefinition("RouteNotBackward", "error"),
    new FactDefinition("RouteAnalysisFailed", "error"),
    new FactDefinition("RouteOverlappingWays", "error"),
    new FactDefinition("RouteSuspiciousWays", "error"),
    new FactDefinition("RouteBroken", "other"),
    new FactDefinition("RouteOneWay", "info"),
    new FactDefinition("RouteNotOneWay", "info"),
    new FactDefinition("NameMissing", "error"),
    new FactDefinition("IgnoreForeignCountry", "other"),
    new FactDefinition("IgnoreNoNodeNetwork", "other"),
    new FactDefinition("IgnoreNoNetworkNodes", "error"),
    new FactDefinition("IgnoreNetworkTaggedAsRoute", "other"),
    new FactDefinition("IgnoreUnsupportedSubset", "other"),
    new FactDefinition("Added", "other"),
    new FactDefinition("BecomeIgnored", "other"),
    new FactDefinition("BecomeOrphan", "other"),
    new FactDefinition("Deleted", "other"),
    new FactDefinition("IgnoreNetworkCollection", "other"),
    new FactDefinition("IgnoreTagBased", "other"),
    new FactDefinition("IntegrityCheck", "other"),
    new FactDefinition("LostBicycleNodeTag", "other"),
    new FactDefinition("LostHikingNodeTag", "other"),
    new FactDefinition("LostHorseNodeTag", "other"),
    new FactDefinition("LostMotorboatNodeTag", "other"),
    new FactDefinition("LostCanoeNodeTag", "other"),
    new FactDefinition("LostInlineSkateNodeTag", "other"),
    new FactDefinition("WasIgnored", "error"),
    new FactDefinition("WasOrphan", "other")
  ]);


  private static readonly keyValues: List<[string,string]> = Facts.facts.map(f => {
    const row: [string, string] = [f.name, f.level];
    return row;
  });
  static readonly factLevels: Map<string, string> = Map(Facts.keyValues);

  static readonly allFactNames = Facts.facts.map(f => f.name);

}
