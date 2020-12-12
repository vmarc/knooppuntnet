import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {SidebarComponent} from '../components/shared/sidebar/sidebar.component';
import {Util} from '../components/shared/util';
import {LongDistanceExampleComponent} from './example/long-distance-example.component';
import {LongDistanceRouteChangePageComponent} from './route/long-distance-route-change-page.component';
import {LongDistanceRouteChangesComponent} from './route/long-distance-route-changes.component';
import {LongDistanceRouteDetailsComponent} from './route/long-distance-route-details.component';
import {LongDistanceRouteMapSidebarComponent} from './route/long-distance-route-map-sidebar.component';
import {LongDistanceRouteMapComponent} from './route/long-distance-route-map.component';
import {LongDistanceRoutesComponent} from './routes/long-distance-routes.component';

const routes: Routes = [
  Util.routePath('routes', LongDistanceRoutesComponent, SidebarComponent),
  Util.routePath('routes/:routeId', LongDistanceRouteDetailsComponent, SidebarComponent),
  Util.routePath('routes/:routeId/map', LongDistanceRouteMapComponent, LongDistanceRouteMapSidebarComponent),
  Util.routePath('routes/:routeId/changes', LongDistanceRouteChangesComponent, SidebarComponent),
  Util.routePath('routes/:routeId/changes/:changeSetId', LongDistanceRouteChangePageComponent, SidebarComponent),
  Util.routePath('example', LongDistanceExampleComponent, SidebarComponent)
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class LongDistanceRoutingModule {
}
