import {NgModule} from '@angular/core';
import {CommonModule} from "@angular/common";
import {RoutePageComponent} from './pages/route/_route-page.component';
import {RouteMembersComponent} from './pages/route/route-members.component';
import {NodePageComponent} from './pages/node/node-page.component';
import {ChangesPageComponent} from './pages/changes/changes-page.component';
import {SubsetOrphanNodesPageComponent} from './pages/subset-orphan-nodes/subset-orphan-nodes-page.component';
import {SubsetOrphanRoutesPageComponent} from './pages/subset-orphan-routes/subset-orphan-routes-page.component';
import {SubsetChangesPageComponent} from './pages/subset-changes/subset-changes-page.component';
import {SubsetNetworksPageComponent} from './pages/subset-networks/subset-networks-page.component';
import {SubsetFactsPageComponent} from './pages/subset-facts/subset-facts-page.component';
import {SubsetFactDetailsPageComponent} from './pages/subset-fact-details/subset-fact-details-page.component';
import {ChangeSetPageComponent} from './pages/changeset/_change-set-page.component';
import {NetworkNodesPageComponent} from './pages/network/nodes/_network-nodes-page.component';
import {NetworkRoutesPageComponent} from './pages/network/routes/_network-routes-page.component';
import {NetworkDetailsPageComponent} from './pages/network/details/_network-details-page.component';
import {NetworkChangesPageComponent} from './pages/network/changes/_network-changes-page.component';
import {NetworkMapPageComponent} from './pages/network/map/_network-map-page.component';
import {NetworkFactsPageComponent} from './pages/network/facts/_network-facts-page.component';
import {OverviewPageComponent} from './pages/overview/overview-page.component';
import {KpnMaterialModule} from "../material/kpn-material.module";
import {AnalysisRoutingModule} from "./analysis-routing.module";
import {SharedModule} from "../components/shared/shared.module";
import {MapModule} from "../components/map/map.module";
import {NodeSummaryComponent} from "./pages/node/node-summary.component";
import {NodeNetworksComponent} from "./pages/node/node-networks.component";
import {NodeRoutesComponent} from "./pages/node/node-routes.component";
import {NetworkNodeTableComponent} from "./pages/network/nodes/network-node-table.component";
import {NetworkNodeAnalysisComponent} from "./pages/network/nodes/network-node-analysis.component";
import {NetworkNodeRoutesComponent} from "./pages/network/nodes/network-node-routes.component";
import {SubsetNetworkListComponent} from './pages/subset-networks/subset-network-list.component';
import {SubsetNetworkTableComponent} from './pages/subset-networks/subset-network-table.component';
import {SubsetNetworkComponent} from './pages/subset-networks/subset-network.component';
import {SubsetNetworkHappyComponent} from './pages/subset-networks/subset-network-happy.component';
import {SubsetOrphanRoutesTableComponent} from './pages/subset-orphan-routes/subset-orphan-routes-table.component';
import {SubsetOrphanRouteComponent} from './pages/subset-orphan-routes/subset-orphan-route.component';
import {SubsetOrphanNodeComponent} from './pages/subset-orphan-nodes/subset-orphan-node.component';
import {SubsetOrphanNodesTableComponent} from './pages/subset-orphan-nodes/subset-orphan-nodes-table.component';
import {ChangesTableComponent} from "./components/changes/changes-table.component";
import {FactRouteNotContiniousComponent} from './fact/descriptions/fact-route-not-continious.component';
import {FactRouteUnusedSegmentsComponent} from './fact/descriptions/fact-route-unused-segments.component';
import {FactRouteNodeMissingInWaysComponent} from './fact/descriptions/fact-route-node-missing-in-ways.component';
import {FactRouteRedundantNodesComponent} from './fact/descriptions/fact-route-redundant-nodes.component';
import {FactRouteWithoutWaysComponent} from './fact/descriptions/fact-route-without-ways.component';
import {FactRouteFixmetodoComponent} from './fact/descriptions/fact-route-fixmetodo.component';
import {FactRouteNameMissingComponent} from './fact/descriptions/fact-route-name-missing.component';
import {FactRouteEndNodeMismatchComponent} from './fact/descriptions/fact-route-end-node-mismatch.component';
import {FactRouteStartNodeMismatchComponent} from './fact/descriptions/fact-route-start-node-mismatch.component';
import {FactRouteTagMissingComponent} from './fact/descriptions/fact-route-tag-missing.component';
import {FactRouteTagInvalidComponent} from './fact/descriptions/fact-route-tag-invalid.component';
import {FactRouteUnexpectedNodeComponent} from './fact/descriptions/fact-route-unexpected-node.component';
import {FactRouteUnexpectedRelationComponent} from './fact/descriptions/fact-route-unexpected-relation.component';
import {FactNetworkExtraMemberNodeComponent} from './fact/descriptions/fact-network-extra-member-node.component';
import {FactNetworkExtraMemberWayComponent} from './fact/descriptions/fact-network-extra-member-way.component';
import {FactNetworkExtraMemberRelationComponent} from './fact/descriptions/fact-network-extra-member-relation.component';
import {FactNodeMemberMissingComponent} from './fact/descriptions/fact-node-member-missing.component';
import {FactIntegrityCheckFailedComponent} from './fact/descriptions/fact-integrity-check-failed.component';
import {FactOrphanRouteComponent} from './fact/descriptions/fact-orphan-route.component';
import {FactOrphanNodeComponent} from './fact/descriptions/fact-orphan-node.component';
import {FactRouteIncompleteComponent} from './fact/descriptions/fact-route-incomplete.component';
import {FactRouteIncompleteOkComponent} from './fact/descriptions/fact-route-incomplete-ok.component';
import {FactRouteUnaccessibleComponent} from './fact/descriptions/fact-route-unaccessible.component';
import {FactRouteInvalidSortingOrderComponent} from './fact/descriptions/fact-route-invalid-sorting-order.component';
import {FactRouteReversedComponent} from './fact/descriptions/fact-route-reversed.component';
import {FactRouteNodeNameMismatchComponent} from './fact/descriptions/fact-route-node-name-mismatch.component';
import {FactRouteNotForwardComponent} from './fact/descriptions/fact-route-not-forward.component';
import {FactRouteNotBackwardComponent} from './fact/descriptions/fact-route-not-backward.component';
import {FactRouteAnalysisFailedComponent} from './fact/descriptions/fact-route-analysis-failed.component';
import {FactRouteOverlappingWaysComponent} from './fact/descriptions/fact-route-overlapping-ways.component';
import {FactRouteSuspiciousWaysComponent} from './fact/descriptions/fact-route-suspicious-ways.component';
import {FactRouteBrokenComponent} from './fact/descriptions/fact-route-broken.component';
import {FactRouteOneWayComponent} from './fact/descriptions/fact-route-one-way.component';
import {FactRouteNotOneWayComponent} from './fact/descriptions/fact-route-not-one-way.component';
import {FactNameMissingComponent} from './fact/descriptions/fact-name-missing.component';
import {FactIgnoreForeignCountryComponent} from './fact/descriptions/fact-ignore-foreign-country.component';
import {FactIgnoreNoNetworkNodesComponent} from './fact/descriptions/fact-ignore-no-network-nodes.component';
import {FactIgnoreUnsupportedSubsetComponent} from './fact/descriptions/fact-ignore-unsupported-subset.component';
import {FactAddedComponent} from './fact/descriptions/fact-added.component';
import {FactBecomeIgnoredComponent} from './fact/descriptions/fact-become-ignored.component';
import {FactBecomeOrphanComponent} from './fact/descriptions/fact-become-orphan.component';
import {FactDeletedComponent} from './fact/descriptions/fact-deleted.component';
import {FactIgnoreNetworkCollectionComponent} from './fact/descriptions/fact-ignore-network-collection.component';
import {FactIntegrityCheckComponent} from './fact/descriptions/fact-integrity-check.component';
import {FactLostBicycleNodeTagComponent} from './fact/descriptions/fact-lost-bicycle-node-tag.component';
import {FactLostHikingNodeTagComponent} from './fact/descriptions/fact-lost-hiking-node-tag.component';
import {FactLostRouteTagsComponent} from './fact/descriptions/fact-lost-route-tags.component';
import {FactWasIgnoredComponent} from './fact/descriptions/fact-was-ignored.component';
import {FactWasOrphanComponent} from './fact/descriptions/fact-was-orphan.component';
import {FactNameComponent} from './fact/fact-name.component';
import {FactDescriptionComponent} from './fact/fact-description.component';
import {MarkdownModule} from "ngx-markdown";
import {NetworkIndicatorComponent} from './pages/network/nodes/indicators/network-indicator.component';
import {ConnectionIndicatorComponent} from './pages/network/nodes/indicators/connection-indicator.component';
import {IntegrityIndicatorComponent} from './pages/network/nodes/indicators/integrity-indicator.component';
import {RoleConnectionIndicatorComponent} from './pages/network/nodes/indicators/role-connection-indicator.component';
import {RouteIndicatorComponent} from './pages/network/nodes/indicators/route-indicator.component';
import {ConnectionIndicatorDialogComponent} from './pages/network/nodes/indicators/connection-indicator-dialog.component';
import {IntegrityIndicatorDialogComponent} from './pages/network/nodes/indicators/integrity-indicator-dialog.component';
import {NetworkIndicatorDialogComponent} from './pages/network/nodes/indicators/network-indicator-dialog.component';
import {RoleConnectionIndicatorDialogComponent} from './pages/network/nodes/indicators/role-connection-indicator-dialog.component';
import {RouteIndicatorDialogComponent} from './pages/network/nodes/indicators/route-indicator-dialog.component';
import {AnalysisSidebarComponent} from "./analysis-sidebar.component";
import {ChangesSetComponent} from "./components/change-set/change-set.component";
import {ChangesSetOrphanRoutesComponent} from "./components/change-set/components/change-set-orphan-routes.component";
import {ChangesSetOrphanNodesComponent} from "./components/change-set/components/change-set-orphan-nodes.component";
import {ChangesSetNetworkComponent} from "./components/change-set/components/change-set-network.component";
import {ChangesSetElementRefComponent} from "./components/change-set/components/change-set-element-ref.component";
import {ChangesSetElementRefsComponent} from "./components/change-set/components/change-set-element-refs.component";
import {ChangeSetOrphanRouteChangesComponent} from "./pages/changeset/change-set-orphan-route-changes.component";
import {ChangeSetOrphanNodeChangesComponent} from "./pages/changeset/change-set-orphan-node-changes.component";
import {ChangeSetNetworkDiffDetailsComponent} from "./pages/changeset/change-set-network-diff-details.component";
import {ChangeSetHeaderComponent} from "./pages/changeset/change-set-header.component";
import {ChangeSetAnalysisComponent} from "./pages/changeset/change-set-analysis.component";
import {NodeDiffsComponent} from "./pages/changeset/node-diffs/_node-diffs.component";
import {NodeDiffsUpdatedComponent} from "./pages/changeset/node-diffs/node-diffs-updated.component";
import {NodeDiffsAddedComponent} from "./pages/changeset/node-diffs/node-diffs-added.component";
import {NodeDiffsRemovedComponent} from "./pages/changeset/node-diffs/node-diffs-removed.component";
import {RouteSummaryComponent} from "./pages/route/route-summary.component";
import {RouteNodeComponent} from "./pages/route/route-node.component";
import {RouteStructureComponent} from "./pages/route/route-structure.component";
import {AnalysisPageComponent} from './pages/analysis/analysis-page.component';
import {NetworkPageHeaderComponent} from "./pages/network/components/network-page-header.component";
import {AnalysisDePageComponent} from "./pages/analysis/analysis-de-page.component";
import {AnalysisNlPageComponent} from "./pages/analysis/analysis-nl-page.component";
import {AnalysisBePageComponent} from "./pages/analysis/analysis-be-page.component";

@NgModule({
  imports: [
    MarkdownModule,
    CommonModule,
    KpnMaterialModule,
    SharedModule,
    MapModule,
    AnalysisRoutingModule
  ],
  declarations: [
    AnalysisSidebarComponent,
    AnalysisPageComponent,
    AnalysisBePageComponent,
    AnalysisDePageComponent,
    AnalysisNlPageComponent,
    ChangeSetPageComponent,
    ChangeSetHeaderComponent,
    ChangeSetAnalysisComponent,
    ChangeSetNetworkDiffDetailsComponent,
    ChangeSetOrphanNodeChangesComponent,
    ChangeSetOrphanRouteChangesComponent,
    NodeDiffsComponent,
    NodeDiffsRemovedComponent,
    NodeDiffsAddedComponent,
    NodeDiffsUpdatedComponent,
    ChangesPageComponent,
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
    RouteMembersComponent,
    RouteSummaryComponent,
    RouteNodeComponent,
    RouteStructureComponent,
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
    ChangesSetComponent,
    ChangesSetNetworkComponent,
    ChangesSetOrphanNodesComponent,
    ChangesSetOrphanRoutesComponent,
    ChangesSetElementRefsComponent,
    ChangesSetElementRefComponent,
    FactNameComponent,
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
    FactWasOrphanComponent,
    FactDescriptionComponent,
    NetworkIndicatorComponent,
    ConnectionIndicatorComponent,
    IntegrityIndicatorComponent,
    RoleConnectionIndicatorComponent,
    RouteIndicatorComponent,
    ConnectionIndicatorDialogComponent,
    IntegrityIndicatorDialogComponent,
    NetworkIndicatorDialogComponent,
    RoleConnectionIndicatorDialogComponent,
    RouteIndicatorDialogComponent,
    NetworkPageHeaderComponent
  ],
  entryComponents: [
    ConnectionIndicatorDialogComponent,
    IntegrityIndicatorDialogComponent,
    NetworkIndicatorDialogComponent,
    RoleConnectionIndicatorDialogComponent,
    RouteIndicatorDialogComponent
  ]
})
export class AnalysisModule {
}
