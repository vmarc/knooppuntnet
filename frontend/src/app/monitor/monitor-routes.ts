import { Routes } from '@angular/router';
import { MonitorChangesPageComponent } from './internal/changes/monitor-changes-page.component';
import { MonitorGroupAddPageComponent } from './internal/group/add/monitor-group-add-page.component';
import { MonitorGroupChangesPageComponent } from './internal/group/changes/monitor-group-changes-page.component';
import { MonitorGroupDeletePageComponent } from './internal/group/delete/monitor-group-delete-page.component';
import { MonitorGroupPageComponent } from './internal/group/details/monitor-group-page.component';
import { MonitorGroupUpdatePageComponent } from './internal/group/update/monitor-group-update-page.component';
import { MonitorGroupsPageComponent } from './internal/groups/monitor-groups-page.component';
import { MonitorWebsocketService } from './internal/monitor-websocket.service';
import { MonitorService } from './internal/monitor.service';
import { MonitorRouteAddPageComponent } from './internal/route/add/monitor-route-add-page.component';
import { MonitorRouteChangePageComponent } from './internal/route/change/monitor-route-change-page.component';
import { MonitorRouteChangesPageComponent } from './internal/route/changes/monitor-route-changes-page.component';
import { MonitorRouteDeletePageComponent } from './internal/route/delete/monitor-route-delete-page.component';
import { MonitorRouteDetailsPageComponent } from './internal/route/details/monitor-route-details-page.component';
import { MonitorRouteGpxDeleteComponent } from './internal/route/gpx/monitor-route-gpx-delete.component';
import { MonitorRouteGpxComponent } from './internal/route/gpx/monitor-route-gpx.component';
import { MonitorRouteMapPageComponent } from './internal/route/map/monitor-route-map-page.component';
import { MonitorRouteGapsComponent } from './internal/route/monitor-route-gaps.component';
import { MonitorRouteUpdatePageComponent } from './internal/route/update/monitor-route-update-page.component';

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
