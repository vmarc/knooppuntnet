import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {MatTableModule} from '@angular/material/table';
import {OlModule} from '../components/ol/ol.module';
import {SharedModule} from '../components/shared/shared.module';
import {LongDistanceExampleComponent} from './example/long-distance-example.component';
import {LongDistanceRoutingModule} from './long-distance-routing.module';
import {LongDistanceRouteComponent} from './route/long-distance-route.component';
import {LongDistanceRoutesTableComponent} from './routes/long-distance-routes-table.component';
import {LongDistanceRoutesComponent} from './routes/long-distance-routes.component';

@NgModule({
  imports: [
    CommonModule,
    LongDistanceRoutingModule,
    SharedModule,
    OlModule,
    MatTableModule,
  ],
  declarations: [
    LongDistanceRoutesComponent,
    LongDistanceRoutesTableComponent,
    LongDistanceRouteComponent,
    LongDistanceExampleComponent
  ],
  exports: [],
})
export class LongDistanceModule {
}
