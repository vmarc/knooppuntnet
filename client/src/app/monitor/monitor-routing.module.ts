import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {SidebarComponent} from '../components/shared/sidebar/sidebar.component';
import {Util} from '../components/shared/util';
import {MonitorRouteChangePageComponent} from './route/changes/monitor-route-change-page.component';
import {MonitorRouteChangesComponent} from './route/changes/monitor-route-changes.component';
import {MonitorRouteDetailsComponent} from './route/details/monitor-route-details.component';
import {MonitorRouteMapSidebarComponent} from './route/map/monitor-route-map-sidebar.component';
import {MonitorRouteMapComponent} from './route/map/monitor-route-map.component';
import {MonitorRoutesComponent} from './routes/monitor-routes.component';

const routes: Routes = [
  Util.routePath('routes', MonitorRoutesComponent, SidebarComponent),
  Util.routePath('routes/:routeId', MonitorRouteDetailsComponent, SidebarComponent),
  Util.routePath('routes/:routeId/map', MonitorRouteMapComponent, MonitorRouteMapSidebarComponent),
  Util.routePath('routes/:routeId/changes', MonitorRouteChangesComponent, SidebarComponent),
  Util.routePath('routes/:routeId/changes/:changeSetId', MonitorRouteChangePageComponent, SidebarComponent)
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class MonitorRoutingModule {
}
