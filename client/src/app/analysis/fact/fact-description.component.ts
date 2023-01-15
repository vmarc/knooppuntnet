import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
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
      <kpn-fact-route-unexpected-relation *ngSwitchCase="'RouteUnexpectedRelation'" [factInfo]="factInfo" />
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
})
export class FactDescriptionComponent {
  @Input() factInfo: FactInfo;
}
