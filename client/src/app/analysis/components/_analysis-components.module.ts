import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {
  MatCheckboxModule,
  MatDialogModule,
  MatDividerModule,
  MatIconModule,
  MatPaginatorModule,
  MatRadioModule,
  MatSlideToggleModule,
  MatSortModule,
  MatTableModule
} from "@angular/material";
import {MarkdownModule} from "ngx-markdown";
import {OlModule} from "../../components/ol/ol.module";
import {SharedModule} from "../../components/shared/shared.module";
import {FactModule} from "../fact/_fact.module";
import {ChangeHeaderComponent} from "./change-set/change-header.component";
import {ChangeSetTagsComponent} from "./change-set/change-set-tags.component";
import {ChangesSetComponent} from "./change-set/change-set.component";
import {ChangesSetElementRefComponent} from "./change-set/components/change-set-element-ref.component";
import {ChangesSetElementRefsComponent} from "./change-set/components/change-set-element-refs.component";
import {ChangesSetNetworkComponent} from "./change-set/components/change-set-network.component";
import {ChangesSetOrphanNodesComponent} from "./change-set/components/change-set-orphan-nodes.component";
import {ChangesSetOrphanRoutesComponent} from "./change-set/components/change-set-orphan-routes.component";
import {ChangesComponent} from "./changes/changes.component";
import {FactCommaListComponent} from "./changes/fact-comma-list.component";
import {FactDiffsComponent} from "./changes/fact-diffs.component";
import {ChangeFilterPeriodComponent} from "./changes/filter/change-filter-period.component";
import {ChangeFilterComponent} from "./changes/filter/change-filter.component";
import {ChangesSidebarComponent} from "./changes/filter/changes-sidebar.component";
import {HistoryIncompleteWarningComponent} from "./changes/history-incomplete-warning.component";
import {NodeChangeDetailComponent} from "./changes/node/node-change-detail.component";
import {NodeChangeMovedComponent} from "./changes/node/node-change-moved.component";
import {RouteChangeDetailComponent} from "./changes/route/route-change-detail.component";
import {RouteChangeWayAddedComponent} from "./changes/route/route-change-way-added.component";
import {RouteChangeWayRemovedComponent} from "./changes/route/route-change-way-removed.component";
import {RouteChangeWayUpdatedComponent} from "./changes/route/route-change-way-updated.component";
import {RouteDiffComponent} from "./changes/route/route-diff.component";
import {RouteNodeDiffComponent} from "./changes/route/route-node-diff.component";
import {TagDiffActionComponent} from "./changes/tag-diff-action.component";
import {TagDiffsTableComponent} from "./changes/tag-diffs-table.component";
import {TagDiffsTextComponent} from "./changes/tag-diffs-text.component";
import {TagDiffsComponent} from "./changes/tag-diffs.component";
import {FilterCheckboxGroupComponent} from "./filter/filter-checkbox-group.component";
import {FilterRadioGroupComponent} from "./filter/filter-radio-group.component";
import {FilterTitleComponent} from "./filter/filter-title.component";
import {FilterComponent} from "./filter/filter.component";

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
    MatRadioModule,
    MatCheckboxModule,
    MatSlideToggleModule,
    OlModule,
    FactModule
  ],
  declarations: [
    ChangeHeaderComponent,
    ChangeSetTagsComponent,
    ChangesSetComponent,
    ChangesSetNetworkComponent,
    ChangesSetOrphanNodesComponent,
    ChangesSetOrphanRoutesComponent,
    ChangesSetElementRefsComponent,
    ChangesSetElementRefComponent,
    HistoryIncompleteWarningComponent,
    TagDiffsComponent,
    TagDiffsTableComponent,
    TagDiffsTextComponent,
    TagDiffActionComponent,
    FactDiffsComponent,
    FactCommaListComponent,
    FilterComponent,
    FilterCheckboxGroupComponent,
    FilterRadioGroupComponent,
    FilterTitleComponent,
    ChangesComponent,
    ChangeFilterComponent,
    ChangeFilterPeriodComponent,
    ChangesSidebarComponent,
    RouteChangeDetailComponent,
    RouteChangeWayAddedComponent,
    RouteChangeWayRemovedComponent,
    RouteChangeWayUpdatedComponent,
    RouteNodeDiffComponent,
    RouteDiffComponent,
    NodeChangeDetailComponent,
    NodeChangeMovedComponent
  ],
  exports: [
    ChangeHeaderComponent,
    ChangeSetTagsComponent,
    ChangesSetComponent,
    ChangesSetNetworkComponent,
    ChangesSetOrphanNodesComponent,
    ChangesSetOrphanRoutesComponent,
    ChangesSetElementRefsComponent,
    ChangesSetElementRefComponent,
    HistoryIncompleteWarningComponent,
    TagDiffsComponent,
    TagDiffsTableComponent,
    TagDiffsTextComponent,
    TagDiffActionComponent,
    FactDiffsComponent,
    FactCommaListComponent,
    FilterComponent,
    FilterCheckboxGroupComponent,
    FilterRadioGroupComponent,
    FilterTitleComponent,
    ChangeFilterComponent,
    ChangesComponent,
    RouteChangeDetailComponent,
    RouteDiffComponent,
    NodeChangeDetailComponent
  ]
})
export class AnalysisComponentsModule {
}
