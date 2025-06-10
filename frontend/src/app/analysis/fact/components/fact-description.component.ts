import { NgComponentOutlet } from '@angular/common';
import { Type } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Fact } from '@api/common/fact';
import { FactAddedComponent } from './descriptions/fact-added.component';
import { FactDeletedComponent } from './descriptions/fact-deleted.component';
import { FactIntegrityCheckFailedComponent } from './descriptions/fact-integrity-check-failed.component';
import { FactIntegrityCheckComponent } from './descriptions/fact-integrity-check.component';
import { FactLostBicycleNodeTagComponent } from './descriptions/fact-lost-bicycle-node-tag.component';
import { FactLostCanoeNodeTagComponent } from './descriptions/fact-lost-canoe-node-tag.component';
import { FactLostHikingNodeTagComponent } from './descriptions/fact-lost-hiking-node-tag.component';
import { FactLostHorseNodeTagComponent } from './descriptions/fact-lost-horse-node-tag.component';
import { FactLostInlineSkateNodeTagComponent } from './descriptions/fact-lost-inline-skate-node-tag.component';
import { FactLostMotorboatNodeTagComponent } from './descriptions/fact-lost-motorboat-node-tag.component';
import { FactLostRouteTagsComponent } from './descriptions/fact-lost-route-tags.component';
import { FactMissingComponent } from './descriptions/fact-missing.component';
import { FactNameMissingComponent } from './descriptions/fact-name-missing.component';
import { FactNetworkExtraMemberNodeComponent } from './descriptions/fact-network-extra-member-node.component';
import { FactNetworkExtraMemberRelationComponent } from './descriptions/fact-network-extra-member-relation.component';
import { FactNetworkExtraMemberWayComponent } from './descriptions/fact-network-extra-member-way.component';
import { FactNodeInvalidSurveyDateComponent } from './descriptions/fact-node-invalid-survey-date.component';
import { FactNodeMemberMissingComponent } from './descriptions/fact-node-member-missing.component';
import { FactOrphanNodeComponent } from './descriptions/fact-orphan-node.component';
import { FactOrphanRouteComponent } from './descriptions/fact-orphan-route.component';
import { FactRouteAnalysisFailedComponent } from './descriptions/fact-route-analysis-failed.component';
import { FactRouteBrokenComponent } from './descriptions/fact-route-broken.component';
import { FactRouteFixmetodoComponent } from './descriptions/fact-route-fixmetodo.component';
import { FactRouteInaccessibleComponent } from './descriptions/fact-route-inaccessible.component';
import { FactRouteIncompleteOkComponent } from './descriptions/fact-route-incomplete-ok.component';
import { FactRouteIncompleteComponent } from './descriptions/fact-route-incomplete.component';
import { FactRouteInvalidSurveyDateComponent } from './descriptions/fact-route-invalid-survey-date.component';
import { FactRouteNameDeprecatedNoteTagComponent } from './descriptions/fact-route-name-deprecated-note-tag.component';
import { FactRouteNameMissingComponent } from './descriptions/fact-route-name-missing.component';
import { FactRouteNodeMissingInWaysComponent } from './descriptions/fact-route-node-missing-in-ways.component';
import { FactRouteNodeNameMismatchComponent } from './descriptions/fact-route-node-name-mismatch.component';
import { FactRouteNotBackwardComponent } from './descriptions/fact-route-not-backward.component';
import { FactRouteNotContiniousComponent } from './descriptions/fact-route-not-continious.component';
import { FactRouteNotForwardComponent } from './descriptions/fact-route-not-forward.component';
import { FactRouteNotOneWayComponent } from './descriptions/fact-route-not-one-way.component';
import { FactRouteOneWayComponent } from './descriptions/fact-route-one-way.component';
import { FactRouteOverlappingWaysComponent } from './descriptions/fact-route-overlapping-ways.component';
import { FactRouteRedundantNodesComponent } from './descriptions/fact-route-redundant-nodes.component';
import { FactRouteSuspiciousWaysComponent } from './descriptions/fact-route-suspicious-ways.component';
import { FactRouteTagInvalidComponent } from './descriptions/fact-route-tag-invalid.component';
import { FactRouteTagMissingComponent } from './descriptions/fact-route-tag-missing.component';
import { FactRouteUnexpectedNodeComponent } from './descriptions/fact-route-unexpected-node.component';
import { FactRouteUnexpectedRelationComponent } from './descriptions/fact-route-unexpected-relation.component';
import { FactRouteUnusedSegmentsComponent } from './descriptions/fact-route-unused-segments.component';
import { FactRouteWithoutNodesComponent } from './descriptions/fact-route-without-nodes.component';
import { FactRouteWithoutWaysComponent } from './descriptions/fact-route-without-ways.component';
import { FactUnexpectedIntegrityCheckComponent } from './descriptions/fact-unexpected-integrity-check.component';
import { FactInfo } from './fact-info';

@Component({
  selector: 'ui-fact-description',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ng-container *ngComponentOutlet="getFactComponent(); inputs: getComponentInputs()" />
  `,
  imports: [NgComponentOutlet],
})
export class FactDescriptionComponent {
  readonly factInfo = input.required<FactInfo>();

  private readonly factComponentMap: Record<string, Type<any>> = {
    Added: FactAddedComponent,
    Deleted: FactDeletedComponent,
    IntegrityCheckFailed: FactIntegrityCheckFailedComponent,
    IntegrityCheck: FactIntegrityCheckComponent,
    LostBicycleNodeTag: FactLostBicycleNodeTagComponent,
    LostCanoeNodeTag: FactLostCanoeNodeTagComponent,
    LostHikingNodeTag: FactLostHikingNodeTagComponent,
    LostHorseNodeTag: FactLostHorseNodeTagComponent,
    LostInlineSkateNodeTag: FactLostInlineSkateNodeTagComponent,
    LostMotorboatNodeTag: FactLostMotorboatNodeTagComponent,
    LostRouteTags: FactLostRouteTagsComponent,
    NameMissing: FactNameMissingComponent,
    NetworkExtraMemberNode: FactNetworkExtraMemberNodeComponent,
    NetworkExtraMemberRelation: FactNetworkExtraMemberRelationComponent,
    NetworkExtraMemberWay: FactNetworkExtraMemberWayComponent,
    NodeMemberMissing: FactNodeMemberMissingComponent,
    OrphanNode: FactOrphanNodeComponent,
    OrphanRoute: FactOrphanRouteComponent,
    RouteAnalysisFailed: FactRouteAnalysisFailedComponent,
    RouteBroken: FactRouteBrokenComponent,
    RouteFixmetodo: FactRouteFixmetodoComponent,
    RouteIncomplete: FactRouteIncompleteComponent,
    RouteIncompleteOk: FactRouteIncompleteOkComponent,
    RouteNameMissing: FactRouteNameMissingComponent,
    RouteNodeMissingInWays: FactRouteNodeMissingInWaysComponent,
    RouteNodeNameMismatch: FactRouteNodeNameMismatchComponent,
    RouteNotBackward: FactRouteNotBackwardComponent,
    RouteNotContinious: FactRouteNotContiniousComponent,
    RouteNotForward: FactRouteNotForwardComponent,
    RouteNotOneWay: FactRouteNotOneWayComponent,
    RouteOneWay: FactRouteOneWayComponent,
    RouteOverlappingWays: FactRouteOverlappingWaysComponent,
    RouteRedundantNodes: FactRouteRedundantNodesComponent,
    RouteSuspiciousWays: FactRouteSuspiciousWaysComponent,
    RouteTagInvalid: FactRouteTagInvalidComponent,
    RouteTagMissing: FactRouteTagMissingComponent,
    RouteInaccessible: FactRouteInaccessibleComponent,
    RouteUnexpectedNode: FactRouteUnexpectedNodeComponent,
    RouteUnexpectedRelation: FactRouteUnexpectedRelationComponent,
    RouteUnusedSegments: FactRouteUnusedSegmentsComponent,
    RouteWithoutNodes: FactRouteWithoutNodesComponent,
    RouteWithoutWays: FactRouteWithoutWaysComponent,
    NodeInvalidSurveyDate: FactNodeInvalidSurveyDateComponent,
    RouteInvalidSurveyDate: FactRouteInvalidSurveyDateComponent,
    RouteNameDeprecatedNoteTag: FactRouteNameDeprecatedNoteTagComponent,
    UnexpectedIntegrityCheck: FactUnexpectedIntegrityCheckComponent,
  };

  private readonly componentsWithFactInfoInput: Set<Fact> = new Set([
    'RouteUnexpectedNode',
    'RouteUnexpectedRelation',
  ]);

  getFactComponent(): Type<any> {
    return this.findFactComponent() || FactMissingComponent;
  }

  getComponentInputs(): Record<string, any> {
    if (this.hasComponentFactInfoInput()) {
      return { factInfo: this.factInfo() };
    }
    return {};
  }

  private findFactComponent(): Type<any> {
    return this.factComponentMap[this.factInfo().fact];
  }

  private hasComponentFactInfoInput(): boolean {
    return !this.findFactComponent() || this.componentsWithFactInfoInput.has(this.factInfo().fact);
  }
}
