import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Routes } from '@angular/router';
import { BaseSidebarComponent } from '@app/base';
import { Util } from '@app/components/shared';
import { PoiAreasPageComponent } from './areas/poi-areas-page.component';
import { PoiDetailPageComponent } from './detail/poi-detail-page.component';
import { PoiLocationPoisPageComponent } from './list/poi-location-pois-page.component';
import { LocationPoisSidebarComponent } from './list/poi-location-pois-sidebar.component';

const routes: Routes = [
  Util.routePath('areas', PoiAreasPageComponent, BaseSidebarComponent),
  Util.routePath(
    ':elementType/:elementId',
    PoiDetailPageComponent,
    BaseSidebarComponent
  ),
  Util.routePath(
    'location/:location/pois',
    PoiLocationPoisPageComponent,
    LocationPoisSidebarComponent
  ),
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PoiRoutingModule {}
