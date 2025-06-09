import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
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
    @switch (factInfo().fact) {
      @case ('Added') {
        <ui-fact-added />
      }
      @case ('Deleted') {
        <ui-fact-deleted />
      }
      @case ('IntegrityCheckFailed') {
        <ui-fact-integrity-check-failed />
      }
      @case ('IntegrityCheck') {
        <ui-fact-integrity-check />
      }
      @case ('LostBicycleNodeTag') {
        <ui-fact-lost-bicycle-node-tag />
      }
      @case ('LostCanoeNodeTag') {
        <ui-fact-lost-canoe-node-tag />
      }
      @case ('LostHikingNodeTag') {
        <ui-fact-lost-hiking-node-tag />
      }
      @case ('LostHorseNodeTag') {
        <ui-fact-lost-horse-node-tag />
      }
      @case ('LostInlineSkateNodeTag') {
        <ui-fact-lost-inline-skate-node-tag />
      }
      @case ('LostMotorboatNodeTag') {
        <ui-fact-lost-motorboat-node-tag />
      }
      @case ('LostRouteTags') {
        <ui-fact-lost-route-tags />
      }
      @case ('NameMissing') {
        <ui-fact-name-missing />
      }
      @case ('NetworkExtraMemberNode') {
        <ui-fact-network-extra-member-node />
      }
      @case ('NetworkExtraMemberRelation') {
        <ui-fact-network-extra-member-relation />
      }
      @case ('NetworkExtraMemberWay') {
        <ui-fact-network-extra-member-way />
      }
      @case ('NodeMemberMissing') {
        <ui-fact-node-member-missing />
      }
      @case ('OrphanNode') {
        <ui-fact-orphan-node />
      }
      @case ('OrphanRoute') {
        <ui-fact-orphan-route />
      }
      @case ('RouteAnalysisFailed') {
        <ui-fact-route-analysis-failed />
      }
      @case ('RouteBroken') {
        <ui-fact-route-broken />
      }
      @case ('RouteFixmetodo') {
        <ui-fact-route-fixmetodo />
      }
      @case ('RouteIncomplete') {
        <ui-fact-route-incomplete />
      }
      @case ('RouteIncompleteOk') {
        <ui-fact-route-incomplete-ok />
      }
      @case ('RouteNameMissing') {
        <ui-fact-route-name-missing />
      }
      @case ('RouteNodeMissingInWays') {
        <ui-fact-route-node-missing-in-ways />
      }
      @case ('RouteNodeNameMismatch') {
        <ui-fact-route-node-name-mismatch />
      }
      @case ('RouteNotBackward') {
        <ui-fact-route-not-backward />
      }
      @case ('RouteNotContinious') {
        <ui-fact-route-not-continious />
      }
      @case ('RouteNotForward') {
        <ui-fact-route-not-forward />
      }
      @case ('RouteNotOneWay') {
        <ui-fact-route-not-one-way />
      }
      @case ('RouteOneWay') {
        <ui-fact-route-one-way />
      }
      @case ('RouteOverlappingWays') {
        <ui-fact-route-overlapping-ways />
      }
      @case ('RouteRedundantNodes') {
        <ui-fact-route-redundant-nodes />
      }
      @case ('RouteSuspiciousWays') {
        <ui-fact-route-suspicious-ways />
      }
      @case ('RouteTagInvalid') {
        <ui-fact-route-tag-invalid />
      }
      @case ('RouteTagMissing') {
        <ui-fact-route-tag-missing />
      }
      @case ('RouteInaccessible') {
        <ui-fact-route-inaccessible />
      }
      @case ('RouteUnexpectedNode') {
        <ui-fact-route-unexpected-node [factInfo]="factInfo()" />
      }
      @case ('RouteUnexpectedRelation') {
        <ui-fact-route-unexpected-relation [factInfo]="factInfo()" />
      }
      @case ('RouteUnusedSegments') {
        <ui-fact-route-unused-segments />
      }
      @case ('RouteWithoutNodes') {
        <ui-fact-route-without-nodes />
      }
      @case ('RouteWithoutWays') {
        <ui-fact-route-without-ways />
      }
      @case ('NodeInvalidSurveyDate') {
        <ui-fact-node-invalid-survey-date />
      }
      @case ('RouteInvalidSurveyDate') {
        <ui-fact-route-invalid-survey-date />
      }
      @case ('RouteNameDeprecatedNoteTag') {
        <ui-fact-route-deprected-note-tag />
      }
      @case ('UnexpectedIntegrityCheck') {
        <ui-fact-unexpected-integrity-check />
      }
      @default {
        <p i18n="@@fact.description-missing">{{ factInfo().fact }} description missing!!</p>
      }
    }
  `,
  imports: [
    FactAddedComponent,
    FactDeletedComponent,
    FactIntegrityCheckComponent,
    FactIntegrityCheckFailedComponent,
    FactLostBicycleNodeTagComponent,
    FactLostCanoeNodeTagComponent,
    FactLostHikingNodeTagComponent,
    FactLostHorseNodeTagComponent,
    FactLostInlineSkateNodeTagComponent,
    FactLostMotorboatNodeTagComponent,
    FactLostRouteTagsComponent,
    FactNameMissingComponent,
    FactNetworkExtraMemberNodeComponent,
    FactNetworkExtraMemberRelationComponent,
    FactNetworkExtraMemberWayComponent,
    FactNodeInvalidSurveyDateComponent,
    FactNodeMemberMissingComponent,
    FactOrphanNodeComponent,
    FactOrphanRouteComponent,
    FactRouteAnalysisFailedComponent,
    FactRouteBrokenComponent,
    FactRouteFixmetodoComponent,
    FactRouteInaccessibleComponent,
    FactRouteIncompleteComponent,
    FactRouteIncompleteOkComponent,
    FactRouteInvalidSurveyDateComponent,
    FactRouteNameMissingComponent,
    FactRouteNodeMissingInWaysComponent,
    FactRouteNodeNameMismatchComponent,
    FactRouteNotBackwardComponent,
    FactRouteNotContiniousComponent,
    FactRouteNotForwardComponent,
    FactRouteNotOneWayComponent,
    FactRouteOneWayComponent,
    FactRouteOverlappingWaysComponent,
    FactRouteRedundantNodesComponent,
    FactRouteSuspiciousWaysComponent,
    FactRouteTagInvalidComponent,
    FactRouteTagMissingComponent,
    FactRouteUnexpectedNodeComponent,
    FactRouteUnexpectedRelationComponent,
    FactRouteUnusedSegmentsComponent,
    FactRouteWithoutNodesComponent,
    FactRouteWithoutWaysComponent,
    FactRouteNameDeprecatedNoteTagComponent,
    FactUnexpectedIntegrityCheckComponent,
  ],
})
export class FactDescriptionComponent {
  readonly factInfo = input.required<FactInfo>();
}
