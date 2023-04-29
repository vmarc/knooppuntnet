import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { AnalysisComponentsModule } from '@app/analysis/components';
import { FactModule } from '@app/analysis/fact';
import { AnalysisStrategyModule } from '@app/analysis/strategy';
import { OlModule } from '@app/components/ol';
import { SharedModule } from '@app/components/shared';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { MarkdownModule } from 'ngx-markdown';
import { SubsetChangesPageComponent } from './changes/_subset-changes-page.component';
import { SubsetChangesSidebarComponent } from './changes/subset-changes-sidebar.component';
import { SubsetPageBreadcrumbComponent } from './components/subset-page-breadcrumb.component';
import { SubsetPageHeaderBlockComponent } from './components/subset-page-header-block.component';
import { SubsetPageMenuComponent } from './components/subset-page-menu.component';
import { SubsetFactDetailsPageComponent } from './fact-details/_subset-fact-details-page.component';
import { SubsetFactDetailsComponent } from './fact-details/subset-fact-details.component';
import { SubsetFactsPageComponent } from './facts/_subset-facts-page.component';
import { SubsetMapPageComponent } from './map/_subset-map-page.component';
import { SubsetMapNetworkDialogComponent } from './map/subset-map-network-dialog.component';
import { SubsetMapComponent } from './map/subset-map.component';
import { SubsetMapService } from './map/subset-map.service';
import { SubsetNetworksPageComponent } from './networks/_subset-networks-page.component';
import { SubsetNetworkHappyComponent } from './networks/subset-network-happy.component';
import { SubsetNetworkListComponent } from './networks/subset-network-list.component';
import { SubsetNetworkTableComponent } from './networks/subset-network-table.component';
import { SubsetNetworkComponent } from './networks/subset-network.component';
import { SubsetOrphanNodesPageComponent } from './orphan-nodes/_subset-orphan-nodes-page.component';
import { SubsetOrphanNodesSidebarComponent } from './orphan-nodes/subset-orphan-nodes-sidebar.component';
import { SubsetOrphanNodesTableComponent } from './orphan-nodes/subset-orphan-nodes-table.component';
import { SubsetOrphanNodesService } from './orphan-nodes/subset-orphan-nodes.service';
import { SubsetOrphanRoutesPageComponent } from './orphan-routes/_subset-orphan-routes-page.component';
import { SubsetOrphanRouteAnalysisComponent } from './orphan-routes/subset-orphan-route-analysis.component';
import { SubsetOrphanRoutesSidebarComponent } from './orphan-routes/subset-orphan-routes-sidebar.component';
import { SubsetOrphanRoutesTableComponent } from './orphan-routes/subset-orphan-routes-table.component';
import { SubsetOrphanRoutesService } from './orphan-routes/subset-orphan-routes.service';
import { SubsetEffects } from './store/subset.effects';
import { subsetReducer } from './store/subset.reducer';
import { subsetFeatureKey } from './store/subset.state';
import { SubsetRoutingModule } from './subset-routing.module';
import { SubsetSidebarComponent } from './subset-sidebar.component';

@NgModule({
  imports: [
    SubsetRoutingModule,
    CommonModule,
    StoreModule.forFeature(subsetFeatureKey, subsetReducer),
    EffectsModule.forFeature([SubsetEffects]),
    MatDividerModule,
    MatIconModule,
    MatPaginatorModule,
    MatSortModule,
    MatTableModule,
    SharedModule,
    FactModule,
    AnalysisComponentsModule,
    OlModule,
    MarkdownModule,
    MatDialogModule,
    MatButtonModule,
    AnalysisStrategyModule,
    SubsetPageBreadcrumbComponent,
    SubsetPageMenuComponent,
    SubsetChangesPageComponent,
    SubsetFactDetailsPageComponent,
    SubsetFactDetailsComponent,
    SubsetFactsPageComponent,
    SubsetNetworksPageComponent,
    SubsetOrphanNodesPageComponent,
    SubsetOrphanRoutesPageComponent,
    SubsetOrphanRouteAnalysisComponent,
    SubsetNetworkListComponent,
    SubsetNetworkTableComponent,
    SubsetNetworkComponent,
    SubsetNetworkHappyComponent,
    SubsetOrphanRoutesTableComponent,
    SubsetOrphanNodesTableComponent,
    SubsetPageHeaderBlockComponent,
    SubsetOrphanNodesSidebarComponent,
    SubsetOrphanRoutesSidebarComponent,
    SubsetChangesSidebarComponent,
    SubsetMapPageComponent,
    SubsetMapComponent,
    SubsetMapNetworkDialogComponent,
    SubsetSidebarComponent,
  ],
  providers: [
    SubsetOrphanNodesService,
    SubsetOrphanRoutesService,
    SubsetMapService,
  ],
})
export class SubsetModule {}