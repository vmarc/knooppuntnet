import { Routes } from '@angular/router';
import { Util } from '@app/components/shared';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { provideState } from '@ngrx/store';
import { MonitorAboutPageComponent } from './about/monitor-about-page.component';
import { MonitorChangesPageComponent } from './changes/monitor-changes-page.component';
import { MonitorChangesPageService } from './changes/monitor-changes-page.service';
import { MonitorGroupAddPageComponent } from './group/add/monitor-group-add-page.component';
import { MonitorGroupAddPageService } from './group/add/monitor-group-add-page.service';
import { MonitorGroupChangesPageComponent } from './group/changes/monitor-group-changes-page.component';
import { MonitorGroupChangesPageService } from './group/changes/monitor-group-changes-page.service';
import { MonitorGroupDeletePageComponent } from './group/delete/monitor-group-delete-page.component';
import { MonitorGroupPageComponent } from './group/details/monitor-group-page.component';
import { MonitorGroupPageService } from './group/details/monitor-group-page.service';
import { MonitorGroupUpdatePageComponent } from './group/update/monitor-group-update-page.component';
import { MonitorGroupsPageComponent } from './groups/monitor-groups-page.component';
import { MonitorGroupsPageService } from './groups/monitor-groups-page.service';
import { MonitorService } from './monitor.service';
import { MonitorRouteAddPageComponent } from './route/add/monitor-route-add-page.component';
import { MonitorRouteChangePageComponent } from './route/change/monitor-route-change-page.component';
import { MonitorRouteChangePageService } from './route/change/monitor-route-change-page.service';
import { MonitorRouteChangesPageComponent } from './route/changes/monitor-route-changes-page.component';
import { MonitorRouteChangesPageService } from './route/changes/monitor-route-changes-page.service';
import { MonitorRouteDeletePageComponent } from './route/delete/monitor-route-delete-page.component';
import { MonitorRouteDetailsPageComponent } from './route/details/monitor-route-details-page.component';
import { MonitorRouteGpxDeleteComponent } from './route/gpx/monitor-route-gpx-delete.component';
import { MonitorRouteGpxComponent } from './route/gpx/monitor-route-gpx.component';
import { MonitorRouteMapPageComponent } from './route/map/monitor-route-map-page.component';
import { MonitorRouteMapSidebarComponent } from './route/map/monitor-route-map-sidebar.component';
import { MonitorRouteMapStateService } from './route/map/monitor-route-map-state.service';
import { MonitorRouteUpdatePageComponent } from './route/update/monitor-route-update-page.component';
import { MonitorRouteUpdatePageService } from './route/update/monitor-route-update-page.service';
import { monitorReducer } from './store/monitor.reducer';
import { monitorFeatureKey } from './store/monitor.state';

export const monitorRoutes: Routes = [
  {
    path: '',
    providers: [
      provideState({
        name: monitorFeatureKey,
        reducer: monitorReducer,
      }),
      MonitorService,
    ],
    children: [
      Util.routePath(
        '',
        MonitorGroupsPageComponent,
        SidebarComponent,
        MonitorGroupsPageService
      ),
      Util.routePath(
        'changes',
        MonitorChangesPageComponent,
        SidebarComponent,
        MonitorChangesPageService
      ),
      Util.routePath('about', MonitorAboutPageComponent, SidebarComponent),
      Util.routePath(
        'groups/:groupName',
        MonitorGroupPageComponent,
        SidebarComponent,
        MonitorGroupPageService
      ),
      Util.routePath(
        'groups/:groupName/changes',
        MonitorGroupChangesPageComponent,
        SidebarComponent,
        MonitorGroupChangesPageService
      ),
      Util.routePath(
        'groups/:groupName/routes/:routeName',
        MonitorRouteDetailsPageComponent,
        SidebarComponent
      ),
      {
        path: 'groups/:groupName/routes/:routeName/map',
        providers: [MonitorRouteMapStateService],
        children: [
          {
            path: '',
            component: MonitorRouteMapPageComponent,
          },
          {
            path: '',
            component: MonitorRouteMapSidebarComponent,
            outlet: 'sidebar',
          },
        ],
      },
      Util.routePath(
        'groups/:groupName/routes/:routeName/gpx',
        MonitorRouteGpxComponent,
        MonitorRouteMapSidebarComponent
      ),
      Util.routePath(
        'groups/:groupName/routes/:routeName/gpx/delete',
        MonitorRouteGpxDeleteComponent,
        MonitorRouteMapSidebarComponent
      ),
      Util.routePath(
        'groups/:groupName/routes/:routeName/changes',
        MonitorRouteChangesPageComponent,
        SidebarComponent,
        MonitorRouteChangesPageService
      ),
      Util.routePath(
        'groups/:groupName/routes/:routeName/changes/:changeSetId/:replicationNumber',
        MonitorRouteChangePageComponent,
        SidebarComponent,
        MonitorRouteChangePageService
      ),
      Util.routePath(
        'admin/groups/add',
        MonitorGroupAddPageComponent,
        SidebarComponent,
        MonitorGroupAddPageService
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
        SidebarComponent,
        MonitorRouteUpdatePageService
      ),
      Util.routePath(
        'admin/groups/:groupName/routes/:routeName/delete',
        MonitorRouteDeletePageComponent,
        SidebarComponent
      ),
    ],
  },
];
