import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {SidebarComponent} from '../components/shared/sidebar/sidebar.component';
import {Util} from '../components/shared/util';
import {MonitorAboutPageComponent} from './about/monitor-about-page.component';
import {MonitorAdminGroupAddPageComponent} from './admin/group/add/monitor-admin-group-add-page.component';
import {MonitorAdminGroupDeletePageComponent} from './admin/group/delete/monitor-admin-group-delete-page.component';
import {MonitorAdminGroupUpdatePageComponent} from './admin/group/update/monitor-admin-group-update-page.component';
import {MonitorAdminRouteAddPageComponent} from './admin/route/add/monitor-admin-route-add-page.component';
import {MonitorAdminRouteDeletePageComponent} from './admin/route/delete/monitor-admin-route-delete-page.component';
import {MonitorAdminRouteUpdatePageComponent} from './admin/route/update/monitor-admin-route-update-page.component';
import {MonitorChangesPageComponent} from './changes/monitor-changes-page.component';
import {MonitorGroupChangesPageComponent} from './group/changes/monitor-group-changes-page.component';
import {MonitorGroupPageComponent} from './group/details/monitor-group-page.component';
import {MonitorGroupsPageComponent} from './groups/monitor-groups-page.component';
import {LongdistanceRouteChangesComponent} from './longdistance/route/changes/longdistance-route-changes.component';
import {LongdistanceRouteDetailsComponent} from './longdistance/route/details/longdistance-route-details.component';
import {LongdistanceRouteMapSidebarComponent} from './longdistance/route/map/longdistance-route-map-sidebar.component';
import {LongdistanceRouteMapComponent} from './longdistance/route/map/longdistance-route-map.component';
import {LongdistanceRoutesComponent} from './longdistance/routes/longdistance-routes.component';
import {MonitorRouteChangePageComponent} from './route/changes/monitor-route-change-page.component';
import {MonitorRouteChangesComponent} from './route/changes/monitor-route-changes.component';
import {MonitorRouteDetailsComponent} from './route/details/monitor-route-details.component';
import {MonitorRouteMapSidebarComponent} from './route/map/monitor-route-map-sidebar.component';
import {MonitorRouteMapComponent} from './route/map/monitor-route-map.component';

const routes: Routes = [

  Util.routePath('long-distance-routes', LongdistanceRoutesComponent, SidebarComponent),
  Util.routePath('long-distance-routes/:routeId', LongdistanceRouteDetailsComponent, SidebarComponent),
  Util.routePath('long-distance-routes/:routeId/map', LongdistanceRouteMapComponent, LongdistanceRouteMapSidebarComponent),
  Util.routePath('long-distance-routes/:routeId/changes', LongdistanceRouteChangesComponent, SidebarComponent),

  Util.routePath('', MonitorGroupsPageComponent, SidebarComponent),
  Util.routePath('changes', MonitorChangesPageComponent, SidebarComponent),
  Util.routePath('about', MonitorAboutPageComponent, SidebarComponent),
  Util.routePath('groups/:groupName', MonitorGroupPageComponent, SidebarComponent),
  Util.routePath('groups/:groupName/changes', MonitorGroupChangesPageComponent, SidebarComponent),
  Util.routePath('groups/:groupName/routes/:routeId', MonitorRouteDetailsComponent, SidebarComponent),
  Util.routePath('groups/:groupName/routes/:routeId/map', MonitorRouteMapComponent, MonitorRouteMapSidebarComponent),
  Util.routePath('groups/:groupName/routes/:routeId/changes', MonitorRouteChangesComponent, SidebarComponent),
  Util.routePath('groups/:groupName/routes/:routeId/changes/:changeSetId/:replicationNumber', MonitorRouteChangePageComponent, SidebarComponent),
  Util.routePath('admin/groups/add', MonitorAdminGroupAddPageComponent, SidebarComponent),
  Util.routePath('admin/groups/:groupName', MonitorAdminGroupUpdatePageComponent, SidebarComponent),
  Util.routePath('admin/groups/:groupName/delete', MonitorAdminGroupDeletePageComponent, SidebarComponent),
  Util.routePath('admin/groups/:groupName/routes/add', MonitorAdminRouteAddPageComponent, SidebarComponent),
  Util.routePath('admin/groups/:groupName/routes/:routeId', MonitorAdminRouteUpdatePageComponent, SidebarComponent),
  Util.routePath('admin/groups/:groupName/routes/:routeId/delete', MonitorAdminRouteDeletePageComponent, SidebarComponent),
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
