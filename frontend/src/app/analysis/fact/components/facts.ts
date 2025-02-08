import { Fact } from '@api/common/fact';
import { FactElement } from './fact-element';
import { FactLevel } from '@api/common/fact-level';

class FactDefinition {
  constructor(
    public fact: Fact,
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
    new FactDefinition('Added', 'other', null),
    new FactDefinition('Deleted', 'other'),
    new FactDefinition('IntegrityCheck', 'other', FactElement.node),
    new FactDefinition('IntegrityCheckFailed', 'error', FactElement.node),
    new FactDefinition('LostBicycleNodeTag', 'other'),
    new FactDefinition('LostCanoeNodeTag', 'other'),
    new FactDefinition('LostHikingNodeTag', 'other'),
    new FactDefinition('LostHorseNodeTag', 'other'),
    new FactDefinition('LostInlineSkateNodeTag', 'other'),
    new FactDefinition('LostMotorboatNodeTag', 'other'),
    new FactDefinition('LostRouteTags', 'other'),
    new FactDefinition('NameMissing', 'error'),
    new FactDefinition('NetworkExtraMemberNode', 'error', FactElement.osmNode),
    new FactDefinition('NetworkExtraMemberRelation', 'error', FactElement.relation),
    new FactDefinition('NetworkExtraMemberWay', 'error', FactElement.way),
    new FactDefinition('NodeMemberMissing', 'info', FactElement.node),
    new FactDefinition('OrphanNode', 'error'),
    new FactDefinition('OrphanRoute', 'error'),
    new FactDefinition('RouteAnalysisFailed', 'error', FactElement.route),
    new FactDefinition('RouteBroken', 'other', FactElement.route),
    new FactDefinition('RouteFixmetodo', 'error', FactElement.route),
    new FactDefinition('RouteIncomplete', 'info', FactElement.route),
    new FactDefinition('RouteIncompleteOk', 'info', FactElement.route),
    new FactDefinition('RouteInvalidSortingOrder', 'info', FactElement.route),
    new FactDefinition('RouteNameMissing', 'error', FactElement.route),
    new FactDefinition('RouteNodeMissingInWays', 'error', FactElement.route),
    new FactDefinition('RouteNodeNameMismatch', 'info', FactElement.route),
    new FactDefinition('RouteNotBackward', 'error', FactElement.route),
    new FactDefinition('RouteNotContinious', 'error', FactElement.route),
    new FactDefinition('RouteNotForward', 'error', FactElement.route),
    new FactDefinition('RouteNotOneWay', 'info', FactElement.route),
    new FactDefinition('RouteOneWay', 'info', FactElement.route),
    new FactDefinition('RouteOverlappingWays', 'error', FactElement.route),
    new FactDefinition('RouteRedundantNodes', 'error', FactElement.route),
    new FactDefinition('RouteSuspiciousWays', 'error', FactElement.route),
    new FactDefinition('RouteTagInvalid', 'error', FactElement.route),
    new FactDefinition('RouteTagMissing', 'error', FactElement.route),
    new FactDefinition('RouteInaccessible', 'info', FactElement.route),
    new FactDefinition('RouteUnexpectedNode', 'error', FactElement.route),
    new FactDefinition('RouteUnexpectedRelation', 'error', FactElement.route),
    new FactDefinition('RouteUnusedSegments', 'error', FactElement.route),
    new FactDefinition('RouteWithoutNodes', 'error', FactElement.route),
    new FactDefinition('RouteWithoutWays', 'error', FactElement.route),
    new FactDefinition('NodeInvalidSurveyDate', 'error', FactElement.node),
    new FactDefinition('RouteInvalidSurveyDate', 'error', FactElement.route),
    new FactDefinition('RouteNameDeprecatedNoteTag', 'info', FactElement.route),
    new FactDefinition('UnexpectedIntegrityCheck', 'error', FactElement.node),
  ];

  static readonly facts: Map<Fact, FactDefinition> = new Map(
    Facts.factDefinitions.map((f) => {
      const row: [Fact, FactDefinition] = [f.fact, f];
      return row;
    })
  );

  static factLevel(fact: Fact): FactLevel {
    return this.facts.get(fact).level;
  }

  static readonly allFactNames = Facts.facts.keys();
}
