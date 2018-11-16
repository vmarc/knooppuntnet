import {Component, Input} from '@angular/core';

@Component({
  selector: 'kpn-fact-description',
  template: `
    <ng-container [ngSwitch]="factName">
      <kpn-fact-added *ngSwitchCase="'Added'"></kpn-fact-added>
      <kpn-fact-become-ignored *ngSwitchCase="'BecomeIgnored'"></kpn-fact-become-ignored>
      <kpn-fact-become-orphan *ngSwitchCase="'BecomeOrphan'"></kpn-fact-become-orphan>
      <kpn-fact-deleted *ngSwitchCase="'Deleted'"></kpn-fact-deleted>
      <kpn-fact-ignore-foreign-country *ngSwitchCase="'IgnoreForeignCountry'"></kpn-fact-ignore-foreign-country>
      <kpn-fact-ignore-network-collection *ngSwitchCase="'IgnoreNetworkCollection'"></kpn-fact-ignore-network-collection>
      <kpn-fact-ignore-no-network-nodes *ngSwitchCase="'IgnoreNoNetworkNodes'"></kpn-fact-ignore-no-network-nodes>
      <kpn-fact-ignore-unsupported-subset *ngSwitchCase="'IgnoreUnsupportedSubset'"></kpn-fact-ignore-unsupported-subset>
      <kpn-fact-integrity-check *ngSwitchCase="'IntegrityCheck'"></kpn-fact-integrity-check>
      <kpn-fact-integrity-check-failed *ngSwitchCase="'IntegrityCheckFailed'"></kpn-fact-integrity-check-failed>
      <kpn-fact-lost-bicycle-node-tag *ngSwitchCase="'LostBicycleNodeTag'"></kpn-fact-lost-bicycle-node-tag>
      <kpn-fact-lost-hiking-node-tag *ngSwitchCase="'LostHikingNodeTag'"></kpn-fact-lost-hiking-node-tag>
      <kpn-fact-lost-route-tags *ngSwitchCase="'LostRouteTags'"></kpn-fact-lost-route-tags>
      <kpn-fact-name-missing *ngSwitchCase="'NameMissing'"></kpn-fact-name-missing>
      <kpn-fact-network-extra-member-node *ngSwitchCase="'NetworkExtraMemberNode'"></kpn-fact-network-extra-member-node>
      <kpn-fact-network-extra-member-relation *ngSwitchCase="'NetworkExtraMemberRelation'"></kpn-fact-network-extra-member-relation>
      <kpn-fact-network-extra-member-way *ngSwitchCase="'NetworkExtraMemberWay'"></kpn-fact-network-extra-member-way>
      <kpn-fact-node-member-missing *ngSwitchCase="'NodeMemberMissing'"></kpn-fact-node-member-missing>
      <kpn-fact-orphan-node *ngSwitchCase="'OrphanNode'"></kpn-fact-orphan-node>
      <kpn-fact-orphan-route *ngSwitchCase="'OrphanRoute'"></kpn-fact-orphan-route>
      <kpn-fact-route-analysis-failed *ngSwitchCase="'RouteAnalysisFailed'"></kpn-fact-route-analysis-failed>
      <kpn-fact-route-broken *ngSwitchCase="'RouteBroken'"></kpn-fact-route-broken>
      <kpn-fact-route-end-node-mismatch *ngSwitchCase="'RouteEndNodeMismatch'"></kpn-fact-route-end-node-mismatch>
      <kpn-fact-route-fixmetodo *ngSwitchCase="'RouteFixmetodo'"></kpn-fact-route-fixmetodo>
      <kpn-fact-route-incomplete *ngSwitchCase="'RouteIncomplete'"></kpn-fact-route-incomplete>
      <kpn-fact-route-incomplete-ok *ngSwitchCase="'RouteIncompleteOk'"></kpn-fact-route-incomplete-ok>
      <kpn-fact-route-invalid-sorting-order *ngSwitchCase="'RouteInvalidSortingOrder'"></kpn-fact-route-invalid-sorting-order>
      <kpn-fact-route-name-missing *ngSwitchCase="'RouteNameMissing'"></kpn-fact-route-name-missing>
      <kpn-fact-route-node-missing-in-ways *ngSwitchCase="'RouteNodeMissingInWays'"></kpn-fact-route-node-missing-in-ways>
      <kpn-fact-route-node-name-mismatch *ngSwitchCase="'RouteNodeNameMismatch'"></kpn-fact-route-node-name-mismatch>
      <kpn-fact-route-not-backward *ngSwitchCase="'RouteNotBackward'"></kpn-fact-route-not-backward>
      <kpn-fact-route-not-continious *ngSwitchCase="'RouteNotContinious'"></kpn-fact-route-not-continious>
      <kpn-fact-route-not-forward *ngSwitchCase="'RouteNotForward'"></kpn-fact-route-not-forward>
      <kpn-fact-route-not-one-way *ngSwitchCase="'RouteNotOneWay'"></kpn-fact-route-not-one-way>
      <kpn-fact-route-one-way *ngSwitchCase="'RouteOneWay'"></kpn-fact-route-one-way>
      <kpn-fact-route-overlapping-ways *ngSwitchCase="'RouteOverlappingWays'"></kpn-fact-route-overlapping-ways>
      <kpn-fact-route-redundant-nodes *ngSwitchCase="'RouteRedundantNodes'"></kpn-fact-route-redundant-nodes>
      <kpn-fact-route-reversed *ngSwitchCase="'RouteReversed'"></kpn-fact-route-reversed>
      <kpn-fact-route-start-node-mismatch *ngSwitchCase="'RouteStartNodeMismatch'"></kpn-fact-route-start-node-mismatch>
      <kpn-fact-route-suspicious-ways *ngSwitchCase="'RouteSuspiciousWays'"></kpn-fact-route-suspicious-ways>
      <kpn-fact-route-tag-invalid *ngSwitchCase="'RouteTagInvalid'"></kpn-fact-route-tag-invalid>
      <kpn-fact-route-tag-missing *ngSwitchCase="'RouteTagMissing'"></kpn-fact-route-tag-missing>
      <kpn-fact-route-unaccessible *ngSwitchCase="'RouteUnaccessible'"></kpn-fact-route-unaccessible>
      <kpn-fact-route-unexpected-node *ngSwitchCase="'RouteUnexpectedNode'"></kpn-fact-route-unexpected-node>
      <kpn-fact-route-unexpected-relation *ngSwitchCase="'RouteUnexpectedRelation'"></kpn-fact-route-unexpected-relation>
      <kpn-fact-route-unused-segments *ngSwitchCase="'RouteUnusedSegments'"></kpn-fact-route-unused-segments>
      <kpn-fact-route-without-ways *ngSwitchCase="'RouteWithoutWays'"></kpn-fact-route-without-ways>
      <kpn-fact-was-ignored *ngSwitchCase="'WasIgnored'"></kpn-fact-was-ignored>
      <kpn-fact-was-orphan *ngSwitchCase="'WasOrphan'"></kpn-fact-was-orphan>
      <ng-container *ngSwitchDefault>{{factName}} description missing</ng-container> <!--beschrijving ontbreekt nog-->
    </ng-container>
  `
})
export class FactDescriptionComponent {
  @Input() factName: string;
}
