import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { MarkdownModule } from 'ngx-markdown';
import { OlModule } from '../../components/ol/ol.module';
import { SharedModule } from '../../components/shared/shared.module';
import { AnalysisComponentsModule } from '../components/analysis-components.module';
import { FactModule } from '../fact/fact.module';
import { SubsetChangesPageComponent } from './changes/_subset-changes-page.component';
import { SubsetChangesSidebarComponent } from './changes/subset-changes-sidebar.component';
import { SubsetChangesService } from './changes/subset-changes.service';
import { SubsetPageBreadcrumbComponent } from './components/subset-page-breadcrumb.component';
import { SubsetPageHeaderBlockComponent } from './components/subset-page-header-block.component';
import { SubsetPageMenuComponent } from './components/subset-page-menu.component';
import { SubsetFactDetailsPageComponent } from './fact-details/_subset-fact-details-page.component';
import { SubsetFactsPageComponent } from './facts/_subset-facts-page.component';
import { SubsetMapPageComponent } from './map/_subset-map-page.component';
import { SubsetMapNetworkDialogComponent } from './map/subset-map-network-dialog.component';
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
import { SubsetOrphanRoutesSidebarComponent } from './orphan-routes/subset-orphan-routes-sidebar.component';
import { SubsetOrphanRoutesTableComponent } from './orphan-routes/subset-orphan-routes-table.component';
import { SubsetOrphanRoutesService } from './orphan-routes/subset-orphan-routes.service';
import { SubsetEffects } from './store/subset.effects';
import { subsetReducer } from './store/subset.reducer';
import { subsetFeatureKey } from './store/subset.state';
import { SubsetRoutingModule } from './subset-routing.module';

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
  ],
  declarations: [
    SubsetPageBreadcrumbComponent,
    SubsetPageMenuComponent,
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
    SubsetOrphanNodesTableComponent,
    SubsetPageHeaderBlockComponent,
    SubsetOrphanNodesSidebarComponent,
    SubsetOrphanRoutesSidebarComponent,
    SubsetChangesSidebarComponent,
    SubsetMapPageComponent,
    SubsetMapNetworkDialogComponent,
  ],
  providers: [
    SubsetOrphanNodesService,
    SubsetOrphanRoutesService,
    SubsetChangesService,
  ],
})
export class SubsetModule {}
