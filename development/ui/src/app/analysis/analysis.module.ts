import {NgModule} from '@angular/core';
import {CommonModule} from "@angular/common";
import {RoutePageComponent} from './pages/route/route-page.component';
import {NodePageComponent} from './pages/node/node-page.component';
import {ChangesPageComponent} from './pages/changes/changes-page.component';
import {SubsetOrphanNodesPageComponent} from './pages/subset-orphan-nodes/subset-orphan-nodes-page.component';
import {SubsetOrphanRoutesPageComponent} from './pages/subset-orphan-routes/subset-orphan-routes-page.component';
import {SubsetChangesPageComponent} from './pages/subset-changes/subset-changes-page.component';
import {SubsetNetworksPageComponent} from './pages/subset-networks/subset-networks-page.component';
import {SubsetFactsPageComponent} from './pages/subset-facts/subset-facts-page.component';
import {SubsetFactDetailsPageComponent} from './pages/subset-fact-details/subset-fact-details-page.component';
import {ChangeSetPageComponent} from './pages/changeset/change-set-page.component';
import {NetworkNodesPageComponent} from './pages/network-nodes/network-nodes-page.component';
import {NetworkRoutesPageComponent} from './pages/network-routes/network-routes-page.component';
import {NetworkDetailsPageComponent} from './pages/network-details/network-details-page.component';
import {NetworkChangesPageComponent} from './pages/network-changes/network-changes-page.component';
import {NetworkMapPageComponent} from './pages/network-map/network-map-page.component';
import {NetworkFactsPageComponent} from './pages/network-facts/network-facts-page.component';
import {OverviewPageComponent} from './pages/overview/overview-page.component';
import {KpnMaterialModule} from "../material/kpn-material.module";
import {MapPageComponent} from "./pages/map/map-page.component";
import {AnalysisRoutingModule} from "./analysis-routing.module";
import {SharedModule} from "../shared/shared.module";
import {MapModule} from "../map/map.module";
import {NodeSummaryComponent} from "./pages/node/node-summary.component";
import {NodeNetworksComponent} from "./pages/node/node-networks.component";
import {NodeRoutesComponent} from "./pages/node/node-routes.component";
import {NetworkNodeTableComponent} from "./pages/network-nodes/network-node-table.component";
import {NetworkNodeAnalysisComponent} from "./pages/network-nodes/network-node-analysis.component";
import {NetworkNodeRoutesComponent} from "./pages/network-nodes/network-node-routes.component";
import {SubsetNetworkListComponent} from './pages/subset-networks/subset-network-list.component';
import {SubsetNetworkTableComponent} from './pages/subset-networks/subset-network-table.component';
import {SubsetNetworkComponent} from './pages/subset-networks/subset-network.component';
import {SubsetNetworkHappyComponent} from './pages/subset-networks/subset-network-happy.component';
import {SubsetOrphanRoutesTableComponent} from './pages/subset-orphan-routes/subset-orphan-routes-table.component';
import {SubsetOrphanRouteComponent} from './pages/subset-orphan-routes/subset-orphan-route.component';
import {SubsetOrphanNodeComponent} from './pages/subset-orphan-nodes/subset-orphan-node.component';
import {SubsetOrphanNodesTableComponent} from './pages/subset-orphan-nodes/subset-orphan-nodes-table.component';
import {ChangesTableComponent} from "./components/changes/changes-table.component";
import {MapDetailComponent} from './pages/map/map-detail.component';
import {MapDetailNodeComponent} from './pages/map/map-detail-node.component';
import {MapDetailRouteComponent} from './pages/map/map-detail-route.component';
import {MapDetailDefaultComponent} from './pages/map/map-detail-default.component';
import {SubsetSidenavComponent} from './components/sidenav/subset-sidenav.component';
import {NetworkSidenavComponent} from './components/sidenav/network-sidenav.component';
import {FactRouteNotContiniousComponent} from './facts/fact-route-not-continious.component';
import {FactRouteUnusedSegmentsComponent} from './facts/fact-route-unused-segments.component';
import {FactRouteNodeMissingInWaysComponent} from './facts/fact-route-node-missing-in-ways.component';
import {FactRouteRedundantNodesComponent} from './facts/fact-route-redundant-nodes.component';
import {FactRouteWithoutWaysComponent} from './facts/fact-route-without-ways.component';
import {FactRouteFixmetodoComponent} from './facts/fact-route-fixmetodo.component';
import {FactRouteNameMissingComponent} from './facts/fact-route-name-missing.component';
import {FactRouteEndNodeMismatchComponent} from './facts/fact-route-end-node-mismatch.component';
import {FactRouteStartNodeMismatchComponent} from './facts/fact-route-start-node-mismatch.component';
import {FactRouteTagMissingComponent} from './facts/fact-route-tag-missing.component';
import {FactRouteTagInvalidComponent} from './facts/fact-route-tag-invalid.component';
import {FactRouteUnexpectedNodeComponent} from './facts/fact-route-unexpected-node.component';
import {FactRouteUnexpectedRelationComponent} from './facts/fact-route-unexpected-relation.component';
import {FactNetworkExtraMemberNodeComponent} from './facts/fact-network-extra-member-node.component';
import {FactNetworkExtraMemberWayComponent} from './facts/fact-network-extra-member-way.component';
import {FactNetworkExtraMemberRelationComponent} from './facts/fact-network-extra-member-relation.component';
import {FactNodeMemberMissingComponent} from './facts/fact-node-member-missing.component';
import {FactIntegrityCheckFailedComponent} from './facts/fact-integrity-check-failed.component';
import {FactOrphanRouteComponent} from './facts/fact-orphan-route.component';
import {FactOrphanNodeComponent} from './facts/fact-orphan-node.component';
import {FactRouteIncompleteComponent} from './facts/fact-route-incomplete.component';
import {FactRouteIncompleteOkComponent} from './facts/fact-route-incomplete-ok.component';
import {FactRouteUnaccessibleComponent} from './facts/fact-route-unaccessible.component';
import {FactRouteInvalidSortingOrderComponent} from './facts/fact-route-invalid-sorting-order.component';
import {FactRouteReversedComponent} from './facts/fact-route-reversed.component';
import {FactRouteNodeNameMismatchComponent} from './facts/fact-route-node-name-mismatch.component';
import {FactRouteNotForwardComponent} from './facts/fact-route-not-forward.component';
import {FactRouteNotBackwardComponent} from './facts/fact-route-not-backward.component';
import {FactRouteAnalysisFailedComponent} from './facts/fact-route-analysis-failed.component';
import {FactRouteOverlappingWaysComponent} from './facts/fact-route-overlapping-ways.component';
import {FactRouteSuspiciousWaysComponent} from './facts/fact-route-suspicious-ways.component';
import {FactRouteBrokenComponent} from './facts/fact-route-broken.component';
import {FactRouteOneWayComponent} from './facts/fact-route-one-way.component';
import {FactRouteNotOneWayComponent} from './facts/fact-route-not-one-way.component';
import {FactNameMissingComponent} from './facts/fact-name-missing.component';
import {FactIgnoreForeignCountryComponent} from './facts/fact-ignore-foreign-country.component';
import {FactIgnoreNoNetworkNodesComponent} from './facts/fact-ignore-no-network-nodes.component';
import {FactIgnoreUnsupportedSubsetComponent} from './facts/fact-ignore-unsupported-subset.component';
import {FactAddedComponent} from './facts/fact-added.component';
import {FactBecomeIgnoredComponent} from './facts/fact-become-ignored.component';
import {FactBecomeOrphanComponent} from './facts/fact-become-orphan.component';
import {FactDeletedComponent} from './facts/fact-deleted.component';
import {FactIgnoreNetworkCollectionComponent} from './facts/fact-ignore-network-collection.component';
import {FactIntegrityCheckComponent} from './facts/fact-integrity-check.component';
import {FactLostBicycleNodeTagComponent} from './facts/fact-lost-bicycle-node-tag.component';
import {FactLostHikingNodeTagComponent} from './facts/fact-lost-hiking-node-tag.component';
import {FactLostRouteTagsComponent} from './facts/fact-lost-route-tags.component';
import {FactWasIgnoredComponent} from './facts/fact-was-ignored.component';
import {FactWasOrphanComponent} from './facts/fact-was-orphan.component';
import {FactComponent} from './facts/fact.component';

@NgModule({
  imports: [
    CommonModule,
    KpnMaterialModule,
    SharedModule,
    MapModule,
    AnalysisRoutingModule
  ],
  declarations: [
    ChangeSetPageComponent,
    ChangesPageComponent,
    MapPageComponent,
    NetworkChangesPageComponent,
    NetworkDetailsPageComponent,
    NetworkFactsPageComponent,
    NetworkMapPageComponent,
    NetworkNodesPageComponent,
    NetworkNodeTableComponent,
    NetworkNodeAnalysisComponent,
    NetworkNodeRoutesComponent,
    NetworkRoutesPageComponent,
    NodePageComponent,
    NodeSummaryComponent,
    NodeNetworksComponent,
    NodeRoutesComponent,
    OverviewPageComponent,
    RoutePageComponent,
    SubsetChangesPageComponent,
    SubsetFactDetailsPageComponent,
    SubsetFactsPageComponent,
    SubsetNetworksPageComponent,
    SubsetOrphanNodesPageComponent,
    SubsetOrphanRoutesPageComponent,
    SubsetNetworkListComponent,
    SubsetNetworkTableComponent,
    SubsetNetworkComponent,
    SubsetNetworkHappyComponent,
    SubsetOrphanRoutesTableComponent,
    SubsetOrphanRouteComponent,
    SubsetOrphanNodeComponent,
    SubsetOrphanNodesTableComponent,
    ChangesTableComponent,
    MapDetailComponent,
    MapDetailNodeComponent,
    MapDetailRouteComponent,
    MapDetailDefaultComponent,
    SubsetSidenavComponent,
    NetworkSidenavComponent,
    FactComponent,
    FactRouteNotContiniousComponent,
    FactRouteUnusedSegmentsComponent,
    FactRouteNodeMissingInWaysComponent,
    FactRouteRedundantNodesComponent,
    FactRouteWithoutWaysComponent,
    FactRouteFixmetodoComponent,
    FactRouteNameMissingComponent,
    FactRouteEndNodeMismatchComponent,
    FactRouteStartNodeMismatchComponent,
    FactRouteTagMissingComponent,
    FactRouteTagInvalidComponent,
    FactRouteUnexpectedNodeComponent,
    FactRouteUnexpectedRelationComponent,
    FactNetworkExtraMemberNodeComponent,
    FactNetworkExtraMemberWayComponent,
    FactNetworkExtraMemberRelationComponent,
    FactNodeMemberMissingComponent,
    FactIntegrityCheckFailedComponent,
    FactOrphanRouteComponent,
    FactOrphanNodeComponent,
    FactRouteIncompleteComponent,
    FactRouteIncompleteOkComponent,
    FactRouteUnaccessibleComponent,
    FactRouteInvalidSortingOrderComponent,
    FactRouteReversedComponent,
    FactRouteNodeNameMismatchComponent,
    FactRouteNotForwardComponent,
    FactRouteNotBackwardComponent,
    FactRouteAnalysisFailedComponent,
    FactRouteOverlappingWaysComponent,
    FactRouteSuspiciousWaysComponent,
    FactRouteBrokenComponent,
    FactRouteOneWayComponent,
    FactRouteNotOneWayComponent,
    FactNameMissingComponent,
    FactIgnoreForeignCountryComponent,
    FactIgnoreNoNetworkNodesComponent,
    FactIgnoreUnsupportedSubsetComponent,
    FactAddedComponent,
    FactBecomeIgnoredComponent,
    FactBecomeOrphanComponent,
    FactDeletedComponent,
    FactIgnoreNetworkCollectionComponent,
    FactIntegrityCheckComponent,
    FactLostBicycleNodeTagComponent,
    FactLostHikingNodeTagComponent,
    FactLostRouteTagsComponent,
    FactWasIgnoredComponent,
    FactWasOrphanComponent
  ]
})
export class AnalysisModule {
}
