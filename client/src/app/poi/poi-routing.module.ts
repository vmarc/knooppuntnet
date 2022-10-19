import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PoiAreasPageComponent } from './areas/poi-areas-page.component';
import { BaseSidebarComponent } from '../base/base-sidebar.component';
import { Util } from '../components/shared/util';
import { PoiDetailPageComponent } from './detail/poi-detail-page.component';
import { LocationPoisSidebarComponent } from './list/poi-location-pois-sidebar.component';
import { PoiLocationPoisPageComponent } from './list/poi-location-pois.component';

const routes: Routes = [
  Util.routePath('areas', PoiAreasPageComponent, BaseSidebarComponent),
  Util.routePath(
    ':elementType/:elementId',
    PoiDetailPageComponent,
    BaseSidebarComponent
  ),
  Util.routePath(
    'location/:country/:location/pois',
    PoiLocationPoisPageComponent,
    LocationPoisSidebarComponent
  ),
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PoiRoutingModule {}
