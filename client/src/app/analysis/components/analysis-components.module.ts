import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatRadioModule } from '@angular/material/radio';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { RouterModule } from '@angular/router';
import { OlModule } from '@app/components/ol/ol.module';
import { SharedModule } from '@app/components/shared/shared.module';
import { MarkdownModule } from 'ngx-markdown';
import { FactModule } from '../fact/fact.module';
import { AnalysisStrategyModule } from '../strategy/strategy.module';
import { ChangeHeaderComponent } from './change-set/change-header.component';
import { ChangeLocationAnalysisSummaryComponent } from './change-set/change-location-analysis-summary.component';
import { ChangeNetworkAnalysisSummaryComponent } from './change-set/change-network-analysis-summary.component';
import { ChangeSetTagsComponent } from './change-set/change-set-tags.component';
import { ChangesSetElementRefComponent } from './change-set/components/change-set-element-ref.component';
import { ChangesSetElementRefsComponent } from './change-set/components/change-set-element-refs.component';
import { ChangesSetNetworkComponent } from './change-set/components/change-set-network.component';
import { ChangesSetOrphanNodesComponent } from './change-set/components/change-set-orphan-nodes.component';
import { ChangesSetOrphanRoutesComponent } from './change-set/components/change-set-orphan-routes.component';
import { ChangesComponent } from './changes/changes.component';
import { FactCommaListComponent } from './changes/fact-comma-list.component';
import { FactDiffsComponent } from './changes/fact-diffs.component';
import { ChangeFilterPeriodComponent } from './changes/filter/change-filter-period.component';
import { ChangeFilterComponent } from './changes/filter/change-filter.component';
import { ChangesSidebarComponent } from './changes/filter/changes-sidebar.component';
import { MonthComponent } from './changes/filter/month.component';
import { NodeChangeDetailComponent } from './changes/node/node-change-detail.component';
import { NodeChangeMovedComponent } from './changes/node/node-change-moved.component';
import { RouteChangeDetailComponent } from './changes/route/route-change-detail.component';
import { RouteChangeWayAddedComponent } from './changes/route/route-change-way-added.component';
import { RouteChangeWayRemovedComponent } from './changes/route/route-change-way-removed.component';
import { RouteChangeWayUpdatedComponent } from './changes/route/route-change-way-updated.component';
import { RouteDiffComponent } from './changes/route/route-diff.component';
import { RouteNodeDiffComponent } from './changes/route/route-node-diff.component';
import { TagDiffActionComponent } from './changes/tag-diff-action.component';
import { TagDiffsTableComponent } from './changes/tag-diffs-table.component';
import { TagDiffsTextComponent } from './changes/tag-diffs-text.component';
import { TagDiffsComponent } from './changes/tag-diffs.component';
import { EditAndPaginatorComponent } from './edit/edit-and-paginator.component';
import { EditDialogComponent } from './edit/edit-dialog.component';
import { EditLinkComponent } from './edit/edit-link.component';
import { FilterCheckboxGroupComponent } from './filter/filter-checkbox-group.component';
import { FilterRadioGroupComponent } from './filter/filter-radio-group.component';
import { FilterTitleComponent } from './filter/filter-title.component';
import { FilterComponent } from './filter/filter.component';
import { RouteAccessibleIndicatorDialogComponent } from './indicators/route/route-accessible-indicator-dialog.component';
import { RouteAccessibleIndicatorComponent } from './indicators/route/route-accessible-indicator.component';
import { RouteConnectionIndicatorDialogComponent } from './indicators/route/route-connection-indicator-dialog.component';
import { RouteConnectionIndicatorComponent } from './indicators/route/route-connection-indicator.component';
import { RouteInvestigateIndicatorDialogComponent } from './indicators/route/route-investigate-indicator-dialog.component';
import { RouteInvestigateIndicatorComponent } from './indicators/route/route-investigate-indicator.component';
import { RouteProposedIndicatorDialogComponent } from './indicators/route/route-proposed-indicator-dialog.component';
import { RouteProposedIndicatorComponent } from './indicators/route/route-proposed-indicator.component';

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
    FactModule,
    RouterModule,
    AnalysisStrategyModule,
    MatButtonModule,
    MatProgressBarModule,
  ],
  declarations: [
    ChangeHeaderComponent,
    ChangeSetTagsComponent,
    ChangeNetworkAnalysisSummaryComponent,
    ChangeLocationAnalysisSummaryComponent,
    ChangesSetNetworkComponent,
    ChangesSetOrphanNodesComponent,
    ChangesSetOrphanRoutesComponent,
    ChangesSetElementRefsComponent,
    ChangesSetElementRefComponent,
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
    NodeChangeMovedComponent,
    RouteAccessibleIndicatorComponent,
    RouteAccessibleIndicatorDialogComponent,
    RouteConnectionIndicatorComponent,
    RouteConnectionIndicatorDialogComponent,
    RouteInvestigateIndicatorComponent,
    RouteInvestigateIndicatorDialogComponent,
    RouteProposedIndicatorComponent,
    RouteProposedIndicatorDialogComponent,
    MonthComponent,
    EditDialogComponent,
    EditLinkComponent,
    EditAndPaginatorComponent,
  ],
  exports: [
    ChangeHeaderComponent,
    ChangeSetTagsComponent,
    ChangeNetworkAnalysisSummaryComponent,
    ChangeLocationAnalysisSummaryComponent,
    ChangesSetNetworkComponent,
    ChangesSetOrphanNodesComponent,
    ChangesSetOrphanRoutesComponent,
    ChangesSetElementRefsComponent,
    ChangesSetElementRefComponent,
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
    NodeChangeDetailComponent,
    RouteAccessibleIndicatorComponent,
    RouteAccessibleIndicatorDialogComponent,
    RouteConnectionIndicatorComponent,
    RouteConnectionIndicatorDialogComponent,
    RouteInvestigateIndicatorComponent,
    RouteInvestigateIndicatorDialogComponent,
    RouteProposedIndicatorComponent,
    RouteProposedIndicatorDialogComponent,
    EditDialogComponent,
    EditLinkComponent,
    EditAndPaginatorComponent,
  ],
})
export class AnalysisComponentsModule {}
