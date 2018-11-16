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
import {RouteNotContiniousComponent} from './facts/route-not-continious.component';
import {RouteUnusedSegmentsComponent} from './facts/route-unused-segments.component';
import {RouteNodeMissingInWaysComponent} from './facts/route-node-missing-in-ways.component';
import {RouteRedundantNodesComponent} from './facts/route-redundant-nodes.component';
import {RouteWithoutWaysComponent} from './facts/route-without-ways.component';
import {RouteFixmetodoComponent} from './facts/route-fixmetodo.component';
import {RouteNameMissingComponent} from './facts/route-name-missing.component';
import {RouteEndNodeMismatchComponent} from './facts/route-end-node-mismatch.component';
import {RouteStartNodeMismatchComponent} from './facts/route-start-node-mismatch.component';
import {RouteTagMissingComponent} from './facts/route-tag-missing.component';
import {RouteTagInvalidComponent} from './facts/route-tag-invalid.component';
import {RouteUnexpectedNodeComponent} from './facts/route-unexpected-node.component';
import {RouteUnexpectedRelationComponent} from './facts/route-unexpected-relation.component';
import {NetworkExtraMemberNodeComponent} from './facts/network-extra-member-node.component';
import {NetworkExtraMemberWayComponent} from './facts/network-extra-member-way.component';
import {NetworkExtraMemberRelationComponent} from './facts/network-extra-member-relation.component';
import {NodeMemberMissingComponent} from './facts/node-member-missing.component';
import {IntegrityCheckFailedComponent} from './facts/integrity-check-failed.component';
import {OrphanRouteComponent} from './facts/orphan-route.component';
import {OrphanNodeComponent} from './facts/orphan-node.component';
import {RouteIncompleteComponent} from './facts/route-incomplete.component';
import {RouteIncompleteOkComponent} from './facts/route-incomplete-ok.component';
import {RouteUnaccessibleComponent} from './facts/route-unaccessible.component';
import {RouteInvalidSortingOrderComponent} from './facts/route-invalid-sorting-order.component';
import {RouteReversedComponent} from './facts/route-reversed.component';
import {RouteNodeNameMismatchComponent} from './facts/route-node-name-mismatch.component';
import {RouteNotForwardComponent} from './facts/route-not-forward.component';
import {RouteNotBackwardComponent} from './facts/route-not-backward.component';
import {RouteAnalysisFailedComponent} from './facts/route-analysis-failed.component';
import {RouteOverlappingWaysComponent} from './facts/route-overlapping-ways.component';
import {RouteSuspiciousWaysComponent} from './facts/route-suspicious-ways.component';
import {RouteBrokenComponent} from './facts/route-broken.component';
import {RouteOneWayComponent} from './facts/route-one-way.component';
import {RouteNotOneWayComponent} from './facts/route-not-one-way.component';
import {NameMissingComponent} from './facts/name-missing.component';
import {IgnoreForeignCountryComponent} from './facts/ignore-foreign-country.component';
import {IgnoreNoNetworkNodesComponent} from './facts/ignore-no-network-nodes.component';
import {IgnoreUnsupportedSubsetComponent} from './facts/ignore-unsupported-subset.component';
import {AddedComponent} from './facts/added.component';
import {BecomeIgnoredComponent} from './facts/become-ignored.component';
import {BecomeOrphanComponent} from './facts/become-orphan.component';
import {DeletedComponent} from './facts/deleted.component';
import {IgnoreNetworkCollectionComponent} from './facts/ignore-network-collection.component';
import {IntegrityCheckComponent} from './facts/integrity-check.component';
import {LostBicycleNodeTagComponent} from './facts/lost-bicycle-node-tag.component';
import {LostHikingNodeTagComponent} from './facts/lost-hiking-node-tag.component';
import {LostRouteTagsComponent} from './facts/lost-route-tags.component';
import {WasIgnoredComponent} from './facts/was-ignored.component';
import {WasOrphanComponent} from './facts/was-orphan.component';

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
    RouteNotContiniousComponent,
    RouteUnusedSegmentsComponent,
    RouteNodeMissingInWaysComponent,
    RouteRedundantNodesComponent,
    RouteWithoutWaysComponent,
    RouteFixmetodoComponent,
    RouteNameMissingComponent,
    RouteEndNodeMismatchComponent,
    RouteStartNodeMismatchComponent,
    RouteTagMissingComponent,
    RouteTagInvalidComponent,
    RouteUnexpectedNodeComponent,
    RouteUnexpectedRelationComponent,
    NetworkExtraMemberNodeComponent,
    NetworkExtraMemberWayComponent,
    NetworkExtraMemberRelationComponent,
    NodeMemberMissingComponent,
    IntegrityCheckFailedComponent,
    OrphanRouteComponent,
    OrphanNodeComponent,
    RouteIncompleteComponent,
    RouteIncompleteOkComponent,
    RouteUnaccessibleComponent,
    RouteInvalidSortingOrderComponent,
    RouteReversedComponent,
    RouteNodeNameMismatchComponent,
    RouteNotForwardComponent,
    RouteNotBackwardComponent,
    RouteAnalysisFailedComponent,
    RouteOverlappingWaysComponent,
    RouteSuspiciousWaysComponent,
    RouteBrokenComponent,
    RouteOneWayComponent,
    RouteNotOneWayComponent,
    NameMissingComponent,
    IgnoreForeignCountryComponent,
    IgnoreNoNetworkNodesComponent,
    IgnoreUnsupportedSubsetComponent,
    AddedComponent,
    BecomeIgnoredComponent,
    BecomeOrphanComponent,
    DeletedComponent,
    IgnoreNetworkCollectionComponent,
    IntegrityCheckComponent,
    LostBicycleNodeTagComponent,
    LostHikingNodeTagComponent,
    LostRouteTagsComponent,
    WasIgnoredComponent,
    WasOrphanComponent
  ]
})
export class AnalysisModule {
}
