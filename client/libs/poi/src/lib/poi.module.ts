import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { AnalysisComponentsModule } from '@app/analysis/components';
import { LocationModule } from '@app/analysis/location';
import { OlModule } from '@app/components/ol';
import { PoiAnalysisModule } from '@app/components/poi';
import { SharedModule } from '@app/components/shared';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { PoiAreasPageComponent } from './areas/poi-areas-page.component';
import { PoiMapComponent } from './areas/poi-map.component';
import { PoiMapService } from './areas/poi-map.service';
import { PoiDetailPageComponent } from './detail/poi-detail-page.component';
import { CountrySelectComponent } from './list/country-select.component';
import { PoiLocationPoiTableComponent } from './list/poi-location-poi-table.component';
import { PoiLocationPoisPageComponent } from './list/poi-location-pois-page.component';
import { LocationPoisSidebarComponent } from './list/poi-location-pois-sidebar.component';
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
    PoiMapComponent,
    PoiDetailPageComponent,
    PoiLocationPoisPageComponent,
    PoiLocationPoiTableComponent,
    LocationPoisSidebarComponent,
    CountrySelectComponent,
  ],
  providers: [PoiService, PoiMapService],
})
export class PoiModule {}
