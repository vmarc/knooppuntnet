import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatDividerModule } from '@angular/material/divider';
import { OlModule } from '../components/ol/ol.module';
import { PoiAnalysisModule } from '../components/poi/poi-analysis.module';
import { SharedModule } from '../components/shared/shared.module';
import { PoiAreasPageComponent } from './areas/poi-areas-page.component';
import { PoiDetailPageComponent } from './detail/poi-detail-page.component';
import { PoiRoutingModule } from './poi-routing.module';

@NgModule({
  imports: [
    CommonModule,
    OlModule,
    SharedModule,
    PoiRoutingModule,
    PoiAnalysisModule,
    MatDividerModule,
  ],
  declarations: [PoiAreasPageComponent, PoiDetailPageComponent],
  providers: [],
})
export class PoiModule {}
