import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {MatDialogModule, MatDividerModule, MatIconModule, MatPaginatorModule, MatSortModule, MatTableModule} from "@angular/material";
import {MarkdownModule} from "ngx-markdown";
import {OlModule} from "../components/ol/ol.module";
import {SharedModule} from "../components/shared/shared.module";
import {SpinnerModule} from "../spinner/spinner.module";
import {AnalysisRoutingModule} from "./analysis-routing.module";
import {AnalysisSidebarComponent} from "./analysis-sidebar.component";
import {ChangeHeaderComponent} from "./components/change-set/change-header.component";
import {ChangeSetInfoComponent} from "./components/change-set/change-set-info.component";
import {ChangesSetComponent} from "./components/change-set/change-set.component";
import {ChangesSetElementRefComponent} from "./components/change-set/components/change-set-element-ref.component";
import {ChangesSetElementRefsComponent} from "./components/change-set/components/change-set-element-refs.component";
import {ChangesSetNetworkComponent} from "./components/change-set/components/change-set-network.component";
import {ChangesSetOrphanNodesComponent} from "./components/change-set/components/change-set-orphan-nodes.component";
import {ChangesSetOrphanRoutesComponent} from "./components/change-set/components/change-set-orphan-routes.component";
import {ChangesTableComponent} from "./components/changes/changes-table.component";
import {FactCommaListComponent} from "./components/changes/fact-comma-list.component";
import {FactDiffsComponent} from "./components/changes/fact-diffs.component";
import {HistoryIncompleteWarningComponent} from "./components/changes/history-incomplete-warning.component";
import {TagDiffsComponent} from "./components/changes/tag-diffs.component";
import {FactAddedComponent} from "./fact/descriptions/fact-added.component";
import {FactBecomeIgnoredComponent} from "./fact/descriptions/fact-become-ignored.component";
import {FactBecomeOrphanComponent} from "./fact/descriptions/fact-become-orphan.component";
import {FactDeletedComponent} from "./fact/descriptions/fact-deleted.component";
import {FactIgnoreForeignCountryComponent} from "./fact/descriptions/fact-ignore-foreign-country.component";
import {FactIgnoreNetworkCollectionComponent} from "./fact/descriptions/fact-ignore-network-collection.component";
import {FactIgnoreNoNetworkNodesComponent} from "./fact/descriptions/fact-ignore-no-network-nodes.component";
import {FactIgnoreUnsupportedSubsetComponent} from "./fact/descriptions/fact-ignore-unsupported-subset.component";
import {FactIntegrityCheckFailedComponent} from "./fact/descriptions/fact-integrity-check-failed.component";
import {FactIntegrityCheckComponent} from "./fact/descriptions/fact-integrity-check.component";
import {FactLostBicycleNodeTagComponent} from "./fact/descriptions/fact-lost-bicycle-node-tag.component";
import {FactLostHikingNodeTagComponent} from "./fact/descriptions/fact-lost-hiking-node-tag.component";
import {FactLostRouteTagsComponent} from "./fact/descriptions/fact-lost-route-tags.component";
import {FactNameMissingComponent} from "./fact/descriptions/fact-name-missing.component";
import {FactNetworkExtraMemberNodeComponent} from "./fact/descriptions/fact-network-extra-member-node.component";
import {FactNetworkExtraMemberRelationComponent} from "./fact/descriptions/fact-network-extra-member-relation.component";
import {FactNetworkExtraMemberWayComponent} from "./fact/descriptions/fact-network-extra-member-way.component";
import {FactNodeMemberMissingComponent} from "./fact/descriptions/fact-node-member-missing.component";
import {FactOrphanNodeComponent} from "./fact/descriptions/fact-orphan-node.component";
import {FactOrphanRouteComponent} from "./fact/descriptions/fact-orphan-route.component";
import {FactRouteAnalysisFailedComponent} from "./fact/descriptions/fact-route-analysis-failed.component";
import {FactRouteBrokenComponent} from "./fact/descriptions/fact-route-broken.component";
import {FactRouteEndNodeMismatchComponent} from "./fact/descriptions/fact-route-end-node-mismatch.component";
import {FactRouteFixmetodoComponent} from "./fact/descriptions/fact-route-fixmetodo.component";
import {FactRouteIncompleteOkComponent} from "./fact/descriptions/fact-route-incomplete-ok.component";
import {FactRouteIncompleteComponent} from "./fact/descriptions/fact-route-incomplete.component";
import {FactRouteInvalidSortingOrderComponent} from "./fact/descriptions/fact-route-invalid-sorting-order.component";
import {FactRouteNameMissingComponent} from "./fact/descriptions/fact-route-name-missing.component";
import {FactRouteNodeMissingInWaysComponent} from "./fact/descriptions/fact-route-node-missing-in-ways.component";
import {FactRouteNodeNameMismatchComponent} from "./fact/descriptions/fact-route-node-name-mismatch.component";
import {FactRouteNotBackwardComponent} from "./fact/descriptions/fact-route-not-backward.component";
import {FactRouteNotContiniousComponent} from "./fact/descriptions/fact-route-not-continious.component";
import {FactRouteNotForwardComponent} from "./fact/descriptions/fact-route-not-forward.component";
import {FactRouteNotOneWayComponent} from "./fact/descriptions/fact-route-not-one-way.component";
import {FactRouteOneWayComponent} from "./fact/descriptions/fact-route-one-way.component";
import {FactRouteOverlappingWaysComponent} from "./fact/descriptions/fact-route-overlapping-ways.component";
import {FactRouteRedundantNodesComponent} from "./fact/descriptions/fact-route-redundant-nodes.component";
import {FactRouteReversedComponent} from "./fact/descriptions/fact-route-reversed.component";
import {FactRouteStartNodeMismatchComponent} from "./fact/descriptions/fact-route-start-node-mismatch.component";
import {FactRouteSuspiciousWaysComponent} from "./fact/descriptions/fact-route-suspicious-ways.component";
import {FactRouteTagInvalidComponent} from "./fact/descriptions/fact-route-tag-invalid.component";
import {FactRouteTagMissingComponent} from "./fact/descriptions/fact-route-tag-missing.component";
import {FactRouteUnaccessibleComponent} from "./fact/descriptions/fact-route-unaccessible.component";
import {FactRouteUnexpectedNodeComponent} from "./fact/descriptions/fact-route-unexpected-node.component";
import {FactRouteUnexpectedRelationComponent} from "./fact/descriptions/fact-route-unexpected-relation.component";
import {FactRouteUnusedSegmentsComponent} from "./fact/descriptions/fact-route-unused-segments.component";
import {FactRouteWithoutWaysComponent} from "./fact/descriptions/fact-route-without-ways.component";
import {FactWasIgnoredComponent} from "./fact/descriptions/fact-was-ignored.component";
import {FactWasOrphanComponent} from "./fact/descriptions/fact-was-orphan.component";
import {FactDescriptionComponent} from "./fact/fact-description.component";
import {FactNameComponent} from "./fact/fact-name.component";
import {AnalysisBePageComponent} from "./pages/analysis/analysis-be-page.component";
import {AnalysisDePageComponent} from "./pages/analysis/analysis-de-page.component";
import {AnalysisNlPageComponent} from "./pages/analysis/analysis-nl-page.component";
import {AnalysisPageComponent} from "./pages/analysis/analysis-page.component";
import {ChangesPageComponent} from "./pages/changes/changes-page.component";
import {ChangeSetPageComponent} from "./pages/changeset/_change-set-page.component";
import {ChangeSetAnalysisComponent} from "./pages/changeset/change-set-analysis.component";
import {ChangeSetHeaderComponent} from "./pages/changeset/change-set-header.component";
import {ChangeSetNetworkDiffDetailsComponent} from "./pages/changeset/change-set-network-diff-details.component";
import {ChangeSetOrphanNodeChangesComponent} from "./pages/changeset/change-set-orphan-node-changes.component";
import {ChangeSetOrphanRouteChangesComponent} from "./pages/changeset/change-set-orphan-route-changes.component";
import {NodeDiffsComponent} from "./pages/changeset/node-diffs/_node-diffs.component";
import {NodeDiffsAddedComponent} from "./pages/changeset/node-diffs/node-diffs-added.component";
import {NodeDiffsRemovedComponent} from "./pages/changeset/node-diffs/node-diffs-removed.component";
import {NodeDiffsUpdatedComponent} from "./pages/changeset/node-diffs/node-diffs-updated.component";
import {FactsPageComponent} from "./pages/facts/_facts-page.component";
import {NetworkChangesPageComponent} from "./pages/network/changes/_network-changes-page.component";
import {NetworkPageHeaderComponent} from "./pages/network/components/network-page-header.component";
import {NetworkDetailsPageComponent} from "./pages/network/details/_network-details-page.component";
import {NetworkFactsPageComponent} from "./pages/network/facts/_network-facts-page.component";
import {NetworkMapPageComponent} from "./pages/network/map/_network-map-page.component";
import {NetworkNodesPageComponent} from "./pages/network/nodes/_network-nodes-page.component";
import {ConnectionIndicatorDialogComponent} from "./pages/network/nodes/indicators/connection-indicator-dialog.component";
import {ConnectionIndicatorComponent} from "./pages/network/nodes/indicators/connection-indicator.component";
import {IntegrityIndicatorDialogComponent} from "./pages/network/nodes/indicators/integrity-indicator-dialog.component";
import {IntegrityIndicatorComponent} from "./pages/network/nodes/indicators/integrity-indicator.component";
import {NetworkIndicatorDialogComponent} from "./pages/network/nodes/indicators/network-indicator-dialog.component";
import {NetworkIndicatorComponent} from "./pages/network/nodes/indicators/network-indicator.component";
import {RoleConnectionIndicatorDialogComponent} from "./pages/network/nodes/indicators/role-connection-indicator-dialog.component";
import {RoleConnectionIndicatorComponent} from "./pages/network/nodes/indicators/role-connection-indicator.component";
import {RouteIndicatorDialogComponent} from "./pages/network/nodes/indicators/route-indicator-dialog.component";
import {RouteIndicatorComponent} from "./pages/network/nodes/indicators/route-indicator.component";
import {NetworkNodeAnalysisComponent} from "./pages/network/nodes/network-node-analysis.component";
import {NetworkNodeRoutesComponent} from "./pages/network/nodes/network-node-routes.component";
import {NetworkNodeTableComponent} from "./pages/network/nodes/network-node-table.component";
import {NetworkRoutesPageComponent} from "./pages/network/routes/_network-routes-page.component";
import {NodeChangesPageComponent} from "./pages/node/changes/_node-changes-page.component";
import {NodeChangeDetailComponent} from "./pages/node/changes/node-change-detail.component";
import {NodeChangeComponent} from "./pages/node/changes/node-change.component";
import {NodePageHeaderComponent} from "./pages/node/components/node-page-header.component";
import {NodeDetailsPageComponent} from "./pages/node/details/_node-details-page.component";
import {NodeNetworkReferencesComponent} from "./pages/node/details/node-network-references.component";
import {NodeOrphanRouteReferencesComponent} from "./pages/node/details/node-orphan-route-references.component";
import {NodeSummaryComponent} from "./pages/node/details/node-summary.component";
import {NodeMapPageComponent} from "./pages/node/map/_node-map-page.component";
import {OverviewPageComponent} from "./pages/overview/_overview-page.component";
import {OverviewTableCellComponent} from "./pages/overview/overview-table-cell.component";
import {OverviewTableHeaderComponent} from "./pages/overview/overview-table-header.component";
import {OverviewTableRowComponent} from "./pages/overview/overview-table-row.component";
import {OverviewTableComponent} from "./pages/overview/overview-table.component";
import {OverviewService} from "./pages/overview/overview.service";
import {StatisticConfigurationComponent} from "./pages/overview/statistic-configuration.component";
import {StatisticConfigurationsComponent} from "./pages/overview/statistic-configurations.component";
import {RouteChangesPageComponent} from "./pages/route/changes/_route-changes-page.component";
import {RouteChangeDetailComponent} from "./pages/route/changes/route-change-detail.component";
import {RouteChangeComponent} from "./pages/route/changes/route-change.component";
import {RouteDiffComponent} from "./pages/route/changes/route-diff.component";
import {RoutePageHeaderComponent} from "./pages/route/components/route-page-header.component";
import {RoutePageComponent} from "./pages/route/details/_route-page.component";
import {RouteMembersComponent} from "./pages/route/details/route-members.component";
import {RouteNodeComponent} from "./pages/route/details/route-node.component";
import {RouteStructureComponent} from "./pages/route/details/route-structure.component";
import {RouteSummaryComponent} from "./pages/route/details/route-summary.component";
import {RouteMapPageComponent} from "./pages/route/map/_route-map-page.component";
import {SubsetChangesPageComponent} from "./pages/subset/changes/_subset-changes-page.component";
import {SubsetPageHeaderComponent} from "./pages/subset/components/subset-page-header.component";
import {SubsetFactDetailsPageComponent} from "./pages/subset/fact-details/_subset-fact-details-page.component";
import {SubsetFactsPageComponent} from "./pages/subset/facts/_subset-facts-page.component";
import {SubsetNetworksPageComponent} from "./pages/subset/networks/_subset-networks-page.component";
import {SubsetNetworkHappyComponent} from "./pages/subset/networks/subset-network-happy.component";
import {SubsetNetworkListComponent} from "./pages/subset/networks/subset-network-list.component";
import {SubsetNetworkTableComponent} from "./pages/subset/networks/subset-network-table.component";
import {SubsetNetworkComponent} from "./pages/subset/networks/subset-network.component";
import {SubsetOrphanNodesPageComponent} from "./pages/subset/orphan-nodes/_subset-orphan-nodes-page.component";
import {SubsetOrphanNodeComponent} from "./pages/subset/orphan-nodes/subset-orphan-node.component";
import {SubsetOrphanNodesTableComponent} from "./pages/subset/orphan-nodes/subset-orphan-nodes-table.component";
import {SubsetOrphanRoutesPageComponent} from "./pages/subset/orphan-routes/_subset-orphan-routes-page.component";
import {SubsetOrphanRouteComponent} from "./pages/subset/orphan-routes/subset-orphan-route.component";
import {SubsetOrphanRoutesTableComponent} from "./pages/subset/orphan-routes/subset-orphan-routes-table.component";
import {NodeNetworkReferenceComponent} from "./pages/node/details/node-network-reference.component";
import {NodeNetworkReferenceStatementComponent} from "./pages/node/details/node-network-reference-statement.component";
import {FactsComponent} from "./fact/facts.component";
import {FactLevelComponent} from "./fact/fact-level.component";
import {FactLostHorseNodeTagComponent} from "./fact/descriptions/fact-lost-horse-node-tag.component";
import {FactLostMotorboatNodeTagComponent} from "./fact/descriptions/fact-lost-motorboat-node-tag.component";
import {FactLostCanoeNodeTagComponent} from "./fact/descriptions/fact-lost-canoe-node-tag.component";
import {FactLostInlineSkateNodeTagComponent} from "./fact/descriptions/fact-lost-inline-skate-node-tag.component";
import {TagDiffActionComponent} from "./components/changes/tag-diff-action.component";
import {TagDiffsTableComponent} from "./components/changes/tag-diffs-table.component";
import {TagDiffsTextComponent} from "./components/changes/tag-diffs-text.component";
import {NodeChangeMovedComponent} from "./pages/node/changes/node-change-moved.component";
import {SubsetPageHeaderBlockComponent} from "./pages/subset/components/subset-page-header-block.component";

@NgModule({
  imports: [
    MarkdownModule,
    CommonModule,
    MatPaginatorModule,
    MatTableModule,
    MatDialogModule,
    MatSortModule,
    MatIconModule,
    MatDialogModule,
    MatDividerModule,
    SharedModule,
    OlModule,
    AnalysisRoutingModule,
    SpinnerModule
  ],
  declarations: [
    AnalysisSidebarComponent,
    AnalysisPageComponent,
    AnalysisBePageComponent,
    AnalysisDePageComponent,
    AnalysisNlPageComponent,
    FactsPageComponent,
    ChangeSetPageComponent,
    ChangeSetHeaderComponent,
    ChangeHeaderComponent,
    ChangeSetInfoComponent,
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
    NodeDetailsPageComponent,
    NodeChangeComponent,
    NodeChangeDetailComponent,
    NodeChangeMovedComponent,
    NodeSummaryComponent,
    NodeNetworkReferencesComponent,
    NodeNetworkReferenceComponent,
    NodeNetworkReferenceStatementComponent,
    NodeOrphanRouteReferencesComponent,
    NodeChangesPageComponent,
    NodeMapPageComponent,
    NodePageHeaderComponent,
    OverviewPageComponent,
    OverviewTableComponent,
    OverviewTableHeaderComponent,
    OverviewTableRowComponent,
    OverviewTableCellComponent,
    StatisticConfigurationsComponent,
    StatisticConfigurationComponent,
    RoutePageComponent,
    RoutePageHeaderComponent,
    RouteMembersComponent,
    RouteSummaryComponent,
    RouteNodeComponent,
    RouteStructureComponent,
    RouteChangesPageComponent,
    RouteMapPageComponent,
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
    FactsComponent,
    FactLevelComponent,
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
    FactLostHorseNodeTagComponent,
    FactLostMotorboatNodeTagComponent,
    FactLostCanoeNodeTagComponent,
    FactLostRouteTagsComponent,
    FactLostInlineSkateNodeTagComponent,
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
    NetworkPageHeaderComponent,
    SubsetPageHeaderComponent,
    SubsetPageHeaderBlockComponent,
    HistoryIncompleteWarningComponent,
    RouteChangeComponent,
    RouteChangeDetailComponent,
    RouteDiffComponent,
    TagDiffsComponent,
    TagDiffsTableComponent,
    TagDiffsTextComponent,
    TagDiffActionComponent,
    FactDiffsComponent,
    FactCommaListComponent
  ],
  entryComponents: [
    ConnectionIndicatorDialogComponent,
    IntegrityIndicatorDialogComponent,
    NetworkIndicatorDialogComponent,
    RoleConnectionIndicatorDialogComponent,
    RouteIndicatorDialogComponent
  ],
  providers: [
    OverviewService
  ]
})
export class AnalysisModule {
}
