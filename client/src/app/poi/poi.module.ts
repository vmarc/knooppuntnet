import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {PoiAreasPageComponent} from './areas/poi-areas-page.component';
import {OlModule} from '../components/ol/ol.module';
import {SharedModule} from '../components/shared/shared.module';
import {PoiRoutingModule} from './poi-routing.module';

@NgModule({
  imports: [
    CommonModule,
    OlModule,
    SharedModule,
    PoiRoutingModule
  ],
  declarations: [
    PoiAreasPageComponent,
  ],
  providers: []
})
export class PoiModule {
}
