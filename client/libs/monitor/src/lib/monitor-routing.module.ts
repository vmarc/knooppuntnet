import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Routes } from '@angular/router';
import { Util } from '@app/components/shared';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { MonitorAboutPageComponent } from './about/monitor-about-page.component';
import { MonitorChangesPageComponent } from './changes/monitor-changes-page.component';
import { MonitorGroupAddPageComponent } from './group/add/monitor-group-add-page.component';
import { MonitorGroupChangesPageComponent } from './group/changes/monitor-group-changes-page.component';
import { MonitorGroupDeletePageComponent } from './group/delete/monitor-group-delete-page.component';
import { MonitorGroupPageComponent } from './group/details/monitor-group-page.component';
import { MonitorGroupUpdatePageComponent } from './group/update/monitor-group-update-page.component';
import { MonitorGroupsPageComponent } from './groups/monitor-groups-page.component';
import { MonitorRouteAddPageComponent } from './route/add/monitor-route-add-page.component';
import { MonitorRouteChangePageComponent } from './route/changes/monitor-route-change-page.component';
import { MonitorRouteChangesPageComponent } from './route/changes/monitor-route-changes-page.component';
import { MonitorRouteDeletePageComponent } from './route/delete/monitor-route-delete-page.component';
import { MonitorRouteDetailsPageComponent } from './route/details/monitor-route-details-page.component';
import { MonitorRouteMapPageComponent } from './route/map/monitor-route-map-page.component';
import { MonitorRouteMapSidebarComponent } from './route/map/monitor-route-map-sidebar.component';
import { MonitorRouteUpdatePageComponent } from './route/update/monitor-route-update-page.component';

const routes: Routes = [
  Util.routePath('', MonitorGroupsPageComponent, SidebarComponent),
  Util.routePath('changes', MonitorChangesPageComponent, SidebarComponent),
  Util.routePath('about', MonitorAboutPageComponent, SidebarComponent),
  Util.routePath(
    'groups/:groupName',
    MonitorGroupPageComponent,
    SidebarComponent
  ),
  Util.routePath(
    'groups/:groupName/changes',
    MonitorGroupChangesPageComponent,
    SidebarComponent
  ),
  Util.routePath(
    'groups/:groupName/routes/:routeName',
    MonitorRouteDetailsPageComponent,
    SidebarComponent
  ),
  Util.routePath(
    'groups/:groupName/routes/:routeName/map',
    MonitorRouteMapPageComponent,
    MonitorRouteMapSidebarComponent
  ),
  Util.routePath(
    'groups/:groupName/routes/:routeName/changes',
    MonitorRouteChangesPageComponent,
    SidebarComponent
  ),
  Util.routePath(
    'groups/:groupName/routes/:routeName/changes/:changeSetId/:replicationNumber',
    MonitorRouteChangePageComponent,
    SidebarComponent
  ),
  Util.routePath(
    'admin/groups/add',
    MonitorGroupAddPageComponent,
    SidebarComponent
  ),
  Util.routePath(
    'admin/groups/:groupName',
    MonitorGroupUpdatePageComponent,
    SidebarComponent
  ),
  Util.routePath(
    'admin/groups/:groupName/delete',
    MonitorGroupDeletePageComponent,
    SidebarComponent
  ),
  Util.routePath(
    'admin/groups/:groupName/routes/add',
    MonitorRouteAddPageComponent,
    SidebarComponent
  ),
  Util.routePath(
    'admin/groups/:groupName/routes/:routeName',
    MonitorRouteUpdatePageComponent,
    SidebarComponent
  ),
  Util.routePath(
    'admin/groups/:groupName/routes/:routeName/delete',
    MonitorRouteDeletePageComponent,
    SidebarComponent
  ),
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class MonitorRoutingModule {}
