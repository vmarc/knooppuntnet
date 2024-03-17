import { FactElement } from './fact-element';
import { FactLevel } from './fact-level';

class FactDefinition {
  constructor(
    public name: string,
    public level: FactLevel,
    public element?: FactElement
  ) {}

  hasNodeRefs(): boolean {
    return this.element === FactElement.node;
  }

  hasOsmNodeRefs(): boolean {
    return this.element === FactElement.osmNode;
  }

  hasOsmWayRefs(): boolean {
    return this.element === FactElement.way;
  }

  hasOsmRelationRefs(): boolean {
    return this.element === FactElement.relation;
  }

  hasRouteRefs(): boolean {
    return this.element === FactElement.route;
  }
}

export class Facts {
  static readonly factDefinitions: FactDefinition[] = [
    new FactDefinition('Added', FactLevel.other, null),
    new FactDefinition('Deleted', FactLevel.other),
    new FactDefinition('IntegrityCheck', FactLevel.other, FactElement.node),
    new FactDefinition('IntegrityCheckFailed', FactLevel.error, FactElement.node),
    new FactDefinition('LostBicycleNodeTag', FactLevel.other),
    new FactDefinition('LostCanoeNodeTag', FactLevel.other),
    new FactDefinition('LostHikingNodeTag', FactLevel.other),
    new FactDefinition('LostHorseNodeTag', FactLevel.other),
    new FactDefinition('LostInlineSkateNodeTag', FactLevel.other),
    new FactDefinition('LostMotorboatNodeTag', FactLevel.other),
    new FactDefinition('LostRouteTags', FactLevel.other),
    new FactDefinition('NameMissing', FactLevel.error),
    new FactDefinition('NetworkExtraMemberNode', FactLevel.error, FactElement.osmNode),
    new FactDefinition('NetworkExtraMemberRelation', FactLevel.error, FactElement.relation),
    new FactDefinition('NetworkExtraMemberWay', FactLevel.error, FactElement.way),
    new FactDefinition('NodeMemberMissing', FactLevel.info, FactElement.node),
    new FactDefinition('OrphanNode', FactLevel.error),
    new FactDefinition('OrphanRoute', FactLevel.error),
    new FactDefinition('RouteAnalysisFailed', FactLevel.error, FactElement.route),
    new FactDefinition('RouteBroken', FactLevel.other, FactElement.route),
    new FactDefinition('RouteFixmetodo', FactLevel.error, FactElement.route),
    new FactDefinition('RouteIncomplete', FactLevel.info, FactElement.route),
    new FactDefinition('RouteIncompleteOk', FactLevel.info, FactElement.route),
    new FactDefinition('RouteInvalidSortingOrder', FactLevel.info, FactElement.route),
    new FactDefinition('RouteNameMissing', FactLevel.error, FactElement.route),
    new FactDefinition('RouteNodeMissingInWays', FactLevel.error, FactElement.route),
    new FactDefinition('RouteNodeNameMismatch', FactLevel.info, FactElement.route),
    new FactDefinition('RouteNotBackward', FactLevel.error, FactElement.route),
    new FactDefinition('RouteNotContinious', FactLevel.error, FactElement.route),
    new FactDefinition('RouteNotForward', FactLevel.error, FactElement.route),
    new FactDefinition('RouteNotOneWay', FactLevel.info, FactElement.route),
    new FactDefinition('RouteOneWay', FactLevel.info, FactElement.route),
    new FactDefinition('RouteOverlappingWays', FactLevel.error, FactElement.route),
    new FactDefinition('RouteRedundantNodes', FactLevel.error, FactElement.route),
    new FactDefinition('RouteReversed', FactLevel.info, FactElement.route),
    new FactDefinition('RouteSuspiciousWays', FactLevel.error, FactElement.route),
    new FactDefinition('RouteTagInvalid', FactLevel.error, FactElement.route),
    new FactDefinition('RouteTagMissing', FactLevel.error, FactElement.route),
    new FactDefinition('RouteInaccessible', FactLevel.info, FactElement.route),
    new FactDefinition('RouteUnexpectedNode', FactLevel.error, FactElement.route),
    new FactDefinition('RouteUnexpectedRelation', FactLevel.error, FactElement.route),
    new FactDefinition('RouteUnusedSegments', FactLevel.error, FactElement.route),
    new FactDefinition('RouteWithoutNodes', FactLevel.error, FactElement.route),
    new FactDefinition('RouteWithoutWays', FactLevel.error, FactElement.route),
    new FactDefinition('NodeInvalidSurveyDate', FactLevel.error, FactElement.node),
    new FactDefinition('RouteInvalidSurveyDate', FactLevel.error, FactElement.route),
    new FactDefinition('RouteNameDeprecatedNoteTag', FactLevel.info, FactElement.route),
  ];

  static readonly facts: Map<string, FactDefinition> = new Map(
    Facts.factDefinitions.map((f) => {
      const row: [string, FactDefinition] = [f.name, f];
      return row;
    })
  );

  static factLevel(factName: string): FactLevel {
    return this.facts.get(factName).level;
  }

  static readonly allFactNames = Facts.facts.keys();

  static factWithName() {
    Facts.facts.get('');
  }
}
