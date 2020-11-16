import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {OlModule} from '../components/ol/ol.module';
import {SharedModule} from '../components/shared/shared.module';
import {PieterComponent} from './pieter.component';
import {PieterRoutingModule} from './pieter-routing.module';

@NgModule({
  imports: [
    CommonModule,
    PieterRoutingModule,
    SharedModule,
    OlModule,
  ],
  declarations: [
    PieterComponent,
  ],
  exports: [
    PieterComponent
  ],
})
export class PieterModule {
}
