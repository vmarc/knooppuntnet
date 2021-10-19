import { List, Map } from 'immutable';
import { FactLevel } from './fact-level';

class FactDefinition {
  constructor(public name: string, public level: FactLevel) {}
}

export class Facts {
  static readonly facts = List<FactDefinition>([
    new FactDefinition('Added', FactLevel.other),
    new FactDefinition('BecomeOrphan', FactLevel.other),
    new FactDefinition('Deleted', FactLevel.other),
    new FactDefinition('IntegrityCheck', FactLevel.other),
    new FactDefinition('IntegrityCheckFailed', FactLevel.error),
    new FactDefinition('LostBicycleNodeTag', FactLevel.other),
    new FactDefinition('LostCanoeNodeTag', FactLevel.other),
    new FactDefinition('LostHikingNodeTag', FactLevel.other),
    new FactDefinition('LostHorseNodeTag', FactLevel.other),
    new FactDefinition('LostInlineSkateNodeTag', FactLevel.other),
    new FactDefinition('LostMotorboatNodeTag', FactLevel.other),
    new FactDefinition('LostRouteTags', FactLevel.other),
    new FactDefinition('NameMissing', FactLevel.error),
    new FactDefinition('NetworkExtraMemberNode', FactLevel.error),
    new FactDefinition('NetworkExtraMemberRelation', FactLevel.error),
    new FactDefinition('NetworkExtraMemberWay', FactLevel.error),
    new FactDefinition('NodeMemberMissing', FactLevel.info),
    new FactDefinition('OrphanNode', FactLevel.error),
    new FactDefinition('OrphanRoute', FactLevel.error),
    new FactDefinition('RouteAnalysisFailed', FactLevel.error),
    new FactDefinition('RouteBroken', FactLevel.other),
    new FactDefinition('RouteFixmetodo', FactLevel.error),
    new FactDefinition('RouteIncomplete', FactLevel.info),
    new FactDefinition('RouteIncompleteOk', FactLevel.info),
    new FactDefinition('RouteInvalidSortingOrder', FactLevel.info),
    new FactDefinition('RouteNameMissing', FactLevel.error),
    new FactDefinition('RouteNodeMissingInWays', FactLevel.error),
    new FactDefinition('RouteNodeNameMismatch', FactLevel.info),
    new FactDefinition('RouteNotBackward', FactLevel.error),
    new FactDefinition('RouteNotContinious', FactLevel.error),
    new FactDefinition('RouteNotForward', FactLevel.error),
    new FactDefinition('RouteNotOneWay', FactLevel.info),
    new FactDefinition('RouteOneWay', FactLevel.info),
    new FactDefinition('RouteOverlappingWays', FactLevel.error),
    new FactDefinition('RouteRedundantNodes', FactLevel.error),
    new FactDefinition('RouteReversed', FactLevel.info),
    new FactDefinition('RouteSuspiciousWays', FactLevel.error),
    new FactDefinition('RouteTagInvalid', FactLevel.error),
    new FactDefinition('RouteTagMissing', FactLevel.error),
    new FactDefinition('RouteInaccessible', FactLevel.info),
    new FactDefinition('RouteUnexpectedNode', FactLevel.error),
    new FactDefinition('RouteUnexpectedRelation', FactLevel.error),
    new FactDefinition('RouteUnusedSegments', FactLevel.error),
    new FactDefinition('RouteWithoutNodes', FactLevel.error),
    new FactDefinition('RouteWithoutWays', FactLevel.error),
    new FactDefinition('WasOrphan', FactLevel.other),
    new FactDefinition('NodeInvalidSurveyDate', FactLevel.error),
    new FactDefinition('RouteInvalidSurveyDate', FactLevel.error),
  ]);

  static readonly factLevels: Map<string, FactLevel> = Map(Facts.keyValues());

  static readonly allFactNames = Facts.facts.map((f) => f.name);

  private static keyValues(): List<[string, FactLevel]> {
    return Facts.facts.map((f) => {
      const row: [string, FactLevel] = [f.name, f.level];
      return row;
    });
  }
}
