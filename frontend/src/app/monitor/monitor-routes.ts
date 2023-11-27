import { Routes } from '@angular/router';
import { MonitorAboutPageComponent } from './about/monitor-about-page.component';
import { MonitorChangesPageComponent } from './changes/monitor-changes-page.component';
import { MonitorGroupAddPageComponent } from './group/add/monitor-group-add-page.component';
import { MonitorGroupChangesPageComponent } from './group/changes/monitor-group-changes-page.component';
import { MonitorGroupDeletePageComponent } from './group/delete/monitor-group-delete-page.component';
import { MonitorGroupPageComponent } from './group/details/monitor-group-page.component';
import { MonitorGroupUpdatePageComponent } from './group/update/monitor-group-update-page.component';
import { MonitorGroupsPageComponent } from './groups/monitor-groups-page.component';
import { MonitorWebsocketService } from './monitor-websocket.service';
import { MonitorService } from './monitor.service';
import { MonitorRouteAddPageComponent } from './route/add/monitor-route-add-page.component';
import { MonitorRouteChangePageComponent } from './route/change/monitor-route-change-page.component';
import { MonitorRouteChangesPageComponent } from './route/changes/monitor-route-changes-page.component';
import { MonitorRouteDeletePageComponent } from './route/delete/monitor-route-delete-page.component';
import { MonitorRouteDetailsPageComponent } from './route/details/monitor-route-details-page.component';
import { MonitorRouteGpxDeleteComponent } from './route/gpx/monitor-route-gpx-delete.component';
import { MonitorRouteGpxComponent } from './route/gpx/monitor-route-gpx.component';
import { MonitorRouteMapPageComponent } from './route/map/monitor-route-map-page.component';
import { MonitorRouteGapsComponent } from './route/monitor-route-gaps.component';
import { MonitorRouteUpdatePageComponent } from './route/update/monitor-route-update-page.component';

export const monitorRoutes: Routes = [
  {
    path: '',
    providers: [MonitorService, MonitorWebsocketService],
    children: [
      {
        path: '',
        component: MonitorGroupsPageComponent,
      },
      {
        path: 'gaps',
        component: MonitorRouteGapsComponent,
      },
      {
        path: 'changes',
        component: MonitorChangesPageComponent,
      },
      {
        path: 'about',
        component: MonitorAboutPageComponent,
      },
      {
        path: 'groups/:groupName',
        component: MonitorGroupPageComponent,
      },
      {
        path: 'groups/:groupName/changes',
        component: MonitorGroupChangesPageComponent,
      },
      {
        path: 'groups/:groupName/routes/:routeName',
        component: MonitorRouteDetailsPageComponent,
      },
      {
        path: 'groups/:groupName/routes/:routeName/map',
        component: MonitorRouteMapPageComponent,
      },
      {
        path: 'groups/:groupName/routes/:routeName/gpx',
        component: MonitorRouteGpxComponent,
      },
      {
        path: 'groups/:groupName/routes/:routeName/gpx/delete',
        component: MonitorRouteGpxDeleteComponent,
      },
      {
        path: 'groups/:groupName/routes/:routeName/changes',
        component: MonitorRouteChangesPageComponent,
      },
      {
        path: 'groups/:groupName/routes/:routeName/changes/:changeSetId/:replicationNumber',
        component: MonitorRouteChangePageComponent,
      },
      {
        path: 'admin/groups/add',
        component: MonitorGroupAddPageComponent,
      },
      {
        path: 'admin/groups/:groupName',
        component: MonitorGroupUpdatePageComponent,
      },
      {
        path: 'admin/groups/:groupName/delete',
        component: MonitorGroupDeletePageComponent,
      },
      {
        path: 'admin/groups/:groupName/routes/add',
        component: MonitorRouteAddPageComponent,
      },
      {
        path: 'admin/groups/:groupName/routes/:routeName',
        component: MonitorRouteUpdatePageComponent,
      },
      {
        path: 'admin/groups/:groupName/routes/:routeName/delete',
        component: MonitorRouteDeletePageComponent,
      },
    ],
  },
];
