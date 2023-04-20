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
import { OlModule } from '@app/components/ol';
import { SharedModule } from '@app/components/shared';
import { MarkdownModule } from 'ngx-markdown';
import { FactModule } from '../fact';
import { AnalysisStrategyModule } from '../strategy';
import { ChangeHeaderComponent } from './change-set';
import { ChangeLocationAnalysisSummaryComponent } from './change-set';
import { ChangeNetworkAnalysisSummaryComponent } from './change-set';
import { ChangeSetTagsComponent } from './change-set';
import { ChangesSetElementRefComponent } from './change-set/components';
import { ChangesSetElementRefsComponent } from './change-set/components';
import { ChangesSetNetworkComponent } from './change-set/components';
import { ChangesSetOrphanNodesComponent } from './change-set/components';
import { ChangesSetOrphanRoutesComponent } from './change-set/components';
import { FactCommaListComponent } from './changes';
import { TagDiffActionComponent } from './changes';
import { TagDiffsTableComponent } from './changes';
import { TagDiffsTextComponent } from './changes';
import { ChangesComponent } from './changes';
import { TagDiffsComponent } from './changes';
import { FactDiffsComponent } from './changes';
import { ChangeFilterPeriodComponent } from './changes/filter';
import { ChangeFilterComponent } from './changes/filter';
import { MonthComponent } from './changes/filter';
import { NodeMovedMapComponent } from './changes/node';
import { NodeChangeDetailComponent } from './changes/node';
import { NodeChangeMovedComponent } from './changes/node';
import { RouteChangeMapComponent } from './changes/route';
import { RouteChangeDetailComponent } from './changes/route';
import { RouteChangeWayAddedComponent } from './changes/route';
import { RouteChangeWayRemovedComponent } from './changes/route';
import { RouteChangeWayUpdatedComponent } from './changes/route';
import { RouteNodeDiffComponent } from './changes/route';
import { RouteDiffComponent } from './changes/route';
import { EditAndPaginatorComponent } from './edit';
import { EditDialogComponent } from './edit';
import { EditLinkComponent } from './edit';
import { FilterCheckboxGroupComponent } from './filter';
import { FilterRadioGroupComponent } from './filter';
import { FilterTitleComponent } from './filter';
import { FilterComponent } from './filter';
import { RouteAccessibleIndicatorDialogComponent } from './indicators/route';
import { RouteAccessibleIndicatorComponent } from './indicators/route';
import { RouteConnectionIndicatorDialogComponent } from './indicators/route';
import { RouteConnectionIndicatorComponent } from './indicators/route';
import { RouteInvestigateIndicatorDialogComponent } from './indicators/route';
import { RouteInvestigateIndicatorComponent } from './indicators/route';
import { RouteProposedIndicatorDialogComponent } from './indicators/route';
import { RouteProposedIndicatorComponent } from './indicators/route';

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
    RouteChangeDetailComponent,
    RouteChangeMapComponent,
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
    NodeMovedMapComponent,
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
