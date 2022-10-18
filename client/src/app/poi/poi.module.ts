import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatDividerModule } from '@angular/material/divider';
import { MatTableModule } from '@angular/material/table';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { AnalysisComponentsModule } from '../analysis/components/analysis-components.module';
import { OlModule } from '../components/ol/ol.module';
import { PoiAnalysisModule } from '../components/poi/poi-analysis.module';
import { SharedModule } from '../components/shared/shared.module';
import { PoiAreasPageComponent } from './areas/poi-areas-page.component';
import { PoiDetailPageComponent } from './detail/poi-detail-page.component';
import { PoiLocationPoiTableComponent } from './list/poi-location-poi-table.component';
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
  ],
  declarations: [
    PoiAreasPageComponent,
    PoiDetailPageComponent,
    PoiLocationPoisPageComponent,
    PoiLocationPoiTableComponent,
  ],
  providers: [PoiService],
})
export class PoiModule {}
