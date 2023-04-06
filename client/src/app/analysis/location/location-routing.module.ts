import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Routes } from '@angular/router';
import { Util } from '@app/components/shared/util';
import { LocationChangesPageComponent } from './changes/location-changes-page.component';
import { LocationEditPageComponent } from './edit/location-edit-page.component';
import { LocationFactsPageComponent } from './facts/location-facts-page.component';
import { LocationSidebarComponent } from './location-sidebar.component';
import { LocationMapPageComponent } from './map/location-map-page.component';
import { LocationNodesPageComponent } from './nodes/location-nodes-page.component';
import { LocationNodesSidebarComponent } from './nodes/location-nodes-sidebar.component';
import { LocationRoutesPageComponent } from './routes/location-routes-page.component';
import { LocationRoutesSidebarComponent } from './routes/location-routes-sidebar.component';
import { LocationSelectionPageComponent } from './selection/location-selection-page.component';
import { LocationSelectionSidebarComponent } from './selection/location-selection-sidebar.component';

const routes: Routes = [
  Util.routePath(
    ':networkType/:country',
    LocationSelectionPageComponent,
    LocationSelectionSidebarComponent
  ),
  Util.routePath(
    ':networkType/:country/:location/nodes',
    LocationNodesPageComponent,
    LocationNodesSidebarComponent
  ),
  Util.routePath(
    ':networkType/:country/:location/routes',
    LocationRoutesPageComponent,
    LocationRoutesSidebarComponent
  ),
  Util.routePath(
    ':networkType/:country/:location/facts',
    LocationFactsPageComponent,
    LocationSidebarComponent
  ),
  Util.routePath(
    ':networkType/:country/:location/map',
    LocationMapPageComponent,
    LocationSidebarComponent
  ),
  Util.routePath(
    ':networkType/:country/:location/changes',
    LocationChangesPageComponent,
    LocationSidebarComponent
  ),
  Util.routePath(
    ':networkType/:country/:location/edit',
    LocationEditPageComponent,
    LocationSidebarComponent
  ),
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class LocationRoutingModule {}
