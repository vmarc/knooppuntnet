import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatRadioModule } from '@angular/material/radio';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { MatTreeModule } from '@angular/material/tree';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { MarkdownModule } from 'ngx-markdown';
import { OlModule } from '@app/components/ol/ol.module';
import { SharedModule } from '@app/components/shared/shared.module';
import { AnalysisComponentsModule } from '../components/analysis-components.module';
import { FactModule } from '../fact/fact.module';
import { AnalysisStrategyModule } from '../strategy/strategy.module';
import { LocationChangesPageComponent } from './changes/location-changes-page.component';
import { LocationChangesComponent } from './changes/location-changes.component';
import { LocationPageBreadcrumbComponent } from './components/location-page-breadcrumb.component';
import { LocationPageHeaderComponent } from './components/location-page-header.component';
import { LocationResponseComponent } from './components/location-response.component';
import { LocationEditPageComponent } from './edit/location-edit-page.component';
import { LocationEditComponent } from './edit/location-edit.component';
import { LocationFactsPageComponent } from './facts/location-facts-page.component';
import { LocationFactsComponent } from './facts/location-facts.component';
import { LocationRoutingModule } from './location-routing.module';
import { LocationSidebarComponent } from './location-sidebar.component';
import { LocationService } from './location.service';
import { LocationMapPageComponent } from './map/location-map-page.component';
import { LocationNodeAnalysisComponent } from './nodes/location-node-analysis.component';
import { LocationNodeFactIndicatorDialogComponent } from './nodes/location-node-fact-indicator-dialog.component';
import { LocationNodeFactIndicatorComponent } from './nodes/location-node-fact-indicator.component';
import { LocationNodeRoutesComponent } from './nodes/location-node-routes.component';
import { LocationNodeTableComponent } from './nodes/location-node-table.component';
import { LocationNodesPageComponent } from './nodes/location-nodes-page.component';
import { LocationNodesSidebarComponent } from './nodes/location-nodes-sidebar.component';
import { LocationNodesComponent } from './nodes/location-nodes.component';
import { LocationRouteAnalysisComponent } from './routes/location-route-analysis';
import { LocationRouteTableComponent } from './routes/location-route-table.component';
import { LocationRoutesPageComponent } from './routes/location-routes-page.component';
import { LocationRoutesSidebarComponent } from './routes/location-routes-sidebar.component';
import { LocationRoutesComponent } from './routes/location-routes.component';
import { LocationModeComponent } from './selection/location-mode.component';
import { LocationModeService } from './selection/location-mode.service';
import { LocationSelectionPageComponent } from './selection/location-selection-page.component';
import { LocationSelectionSidebarComponent } from './selection/location-selection-sidebar.component';
import { LocationSelectionService } from './selection/location-selection.service';
import { LocationSelectorComponent } from './selection/location-selector.component';
import { LocationTreeComponent } from './selection/location-tree.component';
import { LocationEffects } from './store/location.effects';
import { locationReducer } from './store/location.reducer';
import { locationFeatureKey } from './store/location.state';
import { LocationMapService } from '@app/analysis/location/map/location-map.service';
import { LocationMapComponent } from '@app/analysis/location/map/location-map.component';

@NgModule({
  imports: [
    LocationRoutingModule,
    CommonModule,
    StoreModule.forFeature(locationFeatureKey, locationReducer),
    EffectsModule.forFeature([LocationEffects]),
    SharedModule,
    MatDividerModule,
    MatTableModule,
    MatSortModule,
    MatFormFieldModule,
    MatAutocompleteModule,
    MatInputModule,
    ReactiveFormsModule,
    MatIconModule,
    MatTreeModule,
    MatRadioModule,
    MatButtonModule,
    FactModule,
    MatProgressBarModule,
    OlModule,
    MatCheckboxModule,
    MatSlideToggleModule,
    AnalysisComponentsModule,
    MarkdownModule,
    AnalysisStrategyModule,
    MatDialogModule,
  ],
  declarations: [
    LocationPageHeaderComponent,
    LocationPageBreadcrumbComponent,
    LocationNodesPageComponent,
    LocationFactsPageComponent,
    LocationRoutesPageComponent,
    LocationRouteAnalysisComponent,
    LocationMapPageComponent,
    LocationMapComponent,
    LocationChangesPageComponent,
    LocationNodeTableComponent,
    LocationNodeAnalysisComponent,
    LocationNodeRoutesComponent,
    LocationRouteTableComponent,
    LocationRoutesComponent,
    LocationRoutesSidebarComponent,
    LocationNodesComponent,
    LocationNodesSidebarComponent,
    LocationResponseComponent,
    LocationChangesComponent,
    LocationFactsComponent,
    LocationModeComponent,
    LocationSelectionPageComponent,
    LocationSelectionSidebarComponent,
    LocationSelectorComponent,
    LocationTreeComponent,
    LocationEditPageComponent,
    LocationEditComponent,
    LocationNodeFactIndicatorComponent,
    LocationNodeFactIndicatorDialogComponent,
    LocationSidebarComponent,
  ],
  providers: [
    LocationService,
    LocationModeService,
    LocationSelectionService,
    LocationMapService,
  ],
  exports: [LocationSelectorComponent],
})
export class LocationModule {}
