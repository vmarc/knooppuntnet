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
import { FactInfo } from './fact-info';

@Component({
  selector: 'kpn-fact-description',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @switch (factInfo().fact) {
      @case ('Added') {
        <kpn-fact-added />
      }
      @case ('Deleted') {
        <kpn-fact-deleted />
      }
      @case ('IntegrityCheckFailed') {
        <kpn-fact-integrity-check-failed />
      }
      @case ('IntegrityCheck') {
        <kpn-fact-integrity-check />
      }
      @case ('LostBicycleNodeTag') {
        <kpn-fact-lost-bicycle-node-tag />
      }
      @case ('LostCanoeNodeTag') {
        <kpn-fact-lost-canoe-node-tag />
      }
      @case ('LostHikingNodeTag') {
        <kpn-fact-lost-hiking-node-tag />
      }
      @case ('LostHorseNodeTag') {
        <kpn-fact-lost-horse-node-tag />
      }
      @case ('LostInlineSkateNodeTag') {
        <kpn-fact-lost-inline-skate-node-tag />
      }
      @case ('LostMotorboatNodeTag') {
        <kpn-fact-lost-motorboat-node-tag />
      }
      @case ('LostRouteTags') {
        <kpn-fact-lost-route-tags />
      }
      @case ('NameMissing') {
        <kpn-fact-name-missing />
      }
      @case ('NetworkExtraMemberNode') {
        <kpn-fact-network-extra-member-node />
      }
      @case ('NetworkExtraMemberRelation') {
        <kpn-fact-network-extra-member-relation />
      }
      @case ('NetworkExtraMemberWay') {
        <kpn-fact-network-extra-member-way />
      }
      @case ('NodeMemberMissing') {
        <kpn-fact-node-member-missing />
      }
      @case ('OrphanNode') {
        <kpn-fact-orphan-node />
      }
      @case ('OrphanRoute') {
        <kpn-fact-orphan-route />
      }
      @case ('RouteAnalysisFailed') {
        <kpn-fact-route-analysis-failed />
      }
      @case ('RouteBroken') {
        <kpn-fact-route-broken />
      }
      @case ('RouteFixmetodo') {
        <kpn-fact-route-fixmetodo />
      }
      @case ('RouteIncomplete') {
        <kpn-fact-route-incomplete />
      }
      @case ('RouteIncompleteOk') {
        <kpn-fact-route-incomplete-ok />
      }
      @case ('RouteNameMissing') {
        <kpn-fact-route-name-missing />
      }
      @case ('RouteNodeMissingInWays') {
        <kpn-fact-route-node-missing-in-ways />
      }
      @case ('RouteNodeNameMismatch') {
        <kpn-fact-route-node-name-mismatch />
      }
      @case ('RouteNotBackward') {
        <kpn-fact-route-not-backward />
      }
      @case ('RouteNotContinious') {
        <kpn-fact-route-not-continious />
      }
      @case ('RouteNotForward') {
        <kpn-fact-route-not-forward />
      }
      @case ('RouteNotOneWay') {
        <kpn-fact-route-not-one-way />
      }
      @case ('RouteOneWay') {
        <kpn-fact-route-one-way />
      }
      @case ('RouteOverlappingWays') {
        <kpn-fact-route-overlapping-ways />
      }
      @case ('RouteRedundantNodes') {
        <kpn-fact-route-redundant-nodes />
      }
      @case ('RouteSuspiciousWays') {
        <kpn-fact-route-suspicious-ways />
      }
      @case ('RouteTagInvalid') {
        <kpn-fact-route-tag-invalid />
      }
      @case ('RouteTagMissing') {
        <kpn-fact-route-tag-missing />
      }
      @case ('RouteInaccessible') {
        <kpn-fact-route-inaccessible />
      }
      @case ('RouteUnexpectedNode') {
        <kpn-fact-route-unexpected-node [factInfo]="factInfo()" />
      }
      @case ('RouteUnexpectedRelation') {
        <kpn-fact-route-unexpected-relation [factInfo]="factInfo()" />
      }
      @case ('RouteUnusedSegments') {
        <kpn-fact-route-unused-segments />
      }
      @case ('RouteWithoutNodes') {
        <kpn-fact-route-without-nodes />
      }
      @case ('RouteWithoutWays') {
        <kpn-fact-route-without-ways />
      }
      @case ('NodeInvalidSurveyDate') {
        <kpn-fact-node-invalid-survey-date />
      }
      @case ('RouteInvalidSurveyDate') {
        <kpn-fact-route-invalid-survey-date />
      }
      @default {
        <p i18n="@@fact.description-missing">{{ factInfo().fact }} description missing!!</p>
      }
    }
  `,
  standalone: true,
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
  ],
})
export class FactDescriptionComponent {
  factInfo = input<FactInfo | undefined>();
}
