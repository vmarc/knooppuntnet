import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { AnalysisComponentsModule } from '@app/analysis/components/analysis-components.module';
import { LocationModule } from '@app/analysis/location/location.module';
import { OlModule } from '../components/ol/ol.module';
import { PoiAnalysisModule } from '../components/poi/poi-analysis.module';
import { SharedModule } from '../components/shared/shared.module';
import { PoiAreasPageComponent } from './areas/poi-areas-page.component';
import { PoiDetailPageComponent } from './detail/poi-detail-page.component';
import { CountrySelectComponent } from './list/country-select.component';
import { PoiLocationPoiTableComponent } from './list/poi-location-poi-table.component';
import { LocationPoisSidebarComponent } from './list/poi-location-pois-sidebar.component';
import { PoiLocationPoisPageComponent } from './list/poi-location-pois.component';
import { PoiRoutingModule } from './poi-routing.module';
import { PoiService } from './poi.service';
import { PoiEffects } from './store/poi.effects';
import { poiReducer } from './store/poi.reducer';
import { poiFeatureKey } from './store/poi.state';

@NgModule({
  imports: [
    CommonModule,
    OlModule,
    SharedModule,
    PoiRoutingModule,
    PoiAnalysisModule,
    MatDividerModule,
    StoreModule.forFeature(poiFeatureKey, poiReducer),
    EffectsModule.forFeature([PoiEffects]),
    AnalysisComponentsModule,
    MatTableModule,
    MatCheckboxModule,
    LocationModule,
    MatFormFieldModule,
    MatSelectModule,
    ReactiveFormsModule,
  ],
  declarations: [
    PoiAreasPageComponent,
    PoiDetailPageComponent,
    PoiLocationPoisPageComponent,
    PoiLocationPoiTableComponent,
    LocationPoisSidebarComponent,
    CountrySelectComponent,
  ],
  providers: [PoiService],
})
export class PoiModule {}
