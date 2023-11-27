import { NgSwitch, NgSwitchCase, NgSwitchDefault } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
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
    <ng-container [ngSwitch]="factInfo.fact">
      <kpn-fact-added *ngSwitchCase="'Added'" />
      <kpn-fact-deleted *ngSwitchCase="'Deleted'" />
      <kpn-fact-integrity-check-failed *ngSwitchCase="'IntegrityCheckFailed'" />
      <kpn-fact-integrity-check *ngSwitchCase="'IntegrityCheck'" />
      <kpn-fact-lost-bicycle-node-tag *ngSwitchCase="'LostBicycleNodeTag'" />
      <kpn-fact-lost-canoe-node-tag *ngSwitchCase="'LostCanoeNodeTag'" />
      <kpn-fact-lost-hiking-node-tag *ngSwitchCase="'LostHikingNodeTag'" />
      <kpn-fact-lost-horse-node-tag *ngSwitchCase="'LostHorseNodeTag'" />
      <kpn-fact-lost-inline-skate-node-tag *ngSwitchCase="'LostInlineSkateNodeTag'" />
      <kpn-fact-lost-motorboat-node-tag *ngSwitchCase="'LostMotorboatNodeTag'" />
      <kpn-fact-lost-route-tags *ngSwitchCase="'LostRouteTags'" />
      <kpn-fact-name-missing *ngSwitchCase="'NameMissing'" />
      <kpn-fact-network-extra-member-node *ngSwitchCase="'NetworkExtraMemberNode'" />
      <kpn-fact-network-extra-member-relation *ngSwitchCase="'NetworkExtraMemberRelation'" />
      <kpn-fact-network-extra-member-way *ngSwitchCase="'NetworkExtraMemberWay'" />
      <kpn-fact-node-member-missing *ngSwitchCase="'NodeMemberMissing'" />
      <kpn-fact-orphan-node *ngSwitchCase="'OrphanNode'" />
      <kpn-fact-orphan-route *ngSwitchCase="'OrphanRoute'" />
      <kpn-fact-route-analysis-failed *ngSwitchCase="'RouteAnalysisFailed'" />
      <kpn-fact-route-broken *ngSwitchCase="'RouteBroken'" />
      <kpn-fact-route-fixmetodo *ngSwitchCase="'RouteFixmetodo'" />
      <kpn-fact-route-incomplete *ngSwitchCase="'RouteIncomplete'" />
      <kpn-fact-route-incomplete-ok *ngSwitchCase="'RouteIncompleteOk'" />
      <kpn-fact-route-name-missing *ngSwitchCase="'RouteNameMissing'" />
      <kpn-fact-route-node-missing-in-ways *ngSwitchCase="'RouteNodeMissingInWays'" />
      <kpn-fact-route-node-name-mismatch *ngSwitchCase="'RouteNodeNameMismatch'" />
      <kpn-fact-route-not-backward *ngSwitchCase="'RouteNotBackward'" />
      <kpn-fact-route-not-continious *ngSwitchCase="'RouteNotContinious'" />
      <kpn-fact-route-not-forward *ngSwitchCase="'RouteNotForward'" />
      <kpn-fact-route-not-one-way *ngSwitchCase="'RouteNotOneWay'" />
      <kpn-fact-route-one-way *ngSwitchCase="'RouteOneWay'" />
      <kpn-fact-route-overlapping-ways *ngSwitchCase="'RouteOverlappingWays'" />
      <kpn-fact-route-redundant-nodes *ngSwitchCase="'RouteRedundantNodes'" />
      <kpn-fact-route-suspicious-ways *ngSwitchCase="'RouteSuspiciousWays'" />
      <kpn-fact-route-tag-invalid *ngSwitchCase="'RouteTagInvalid'" />
      <kpn-fact-route-tag-missing *ngSwitchCase="'RouteTagMissing'" />
      <kpn-fact-route-inaccessible *ngSwitchCase="'RouteInaccessible'" />
      <kpn-fact-route-unexpected-node *ngSwitchCase="'RouteUnexpectedNode'" [factInfo]="factInfo" />
      <kpn-fact-route-unexpected-relation
        *ngSwitchCase="'RouteUnexpectedRelation'"
        [factInfo]="factInfo"
      />
      <kpn-fact-route-unused-segments *ngSwitchCase="'RouteUnusedSegments'" />
      <kpn-fact-route-without-nodes *ngSwitchCase="'RouteWithoutNodes'" />
      <kpn-fact-route-without-ways *ngSwitchCase="'RouteWithoutWays'" />
      <kpn-fact-node-invalid-survey-date *ngSwitchCase="'NodeInvalidSurveyDate'" />
      <kpn-fact-route-invalid-survey-date *ngSwitchCase="'RouteInvalidSurveyDate'" />
      <p *ngSwitchDefault i18n="@@fact.description-missing">
        {{ factInfo.fact }} description missing!!
      </p>
    </ng-container>
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
    NgSwitch,
    NgSwitchCase,
    NgSwitchDefault,
  ],
})
export class FactDescriptionComponent {
  @Input() factInfo: FactInfo;
}
