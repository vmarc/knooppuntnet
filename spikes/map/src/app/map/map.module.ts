import {NgModule} from '@angular/core';
import {MapRoutingModule} from './map-routing.module';
import {MapComponent} from './map.component';

@NgModule({
  imports: [
    MapRoutingModule,
  ],
  declarations: [
    MapComponent,
  ]
})
export class MapModule {
}
