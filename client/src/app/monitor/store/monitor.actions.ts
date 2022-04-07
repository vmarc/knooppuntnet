import { Bounds } from '@api/common/bounds';
import { MonitorChangesPage } from '@api/common/monitor/monitor-changes-page';
import { MonitorGroup } from '@api/common/monitor/monitor-group';
import { MonitorGroupChangesPage } from '@api/common/monitor/monitor-group-changes-page';
import { MonitorGroupPage } from '@api/common/monitor/monitor-group-page';
import { MonitorGroupsPage } from '@api/common/monitor/monitor-groups-page';
import { MonitorRouteAdd } from '@api/common/monitor/monitor-route-add';
import { MonitorRouteChangePage } from '@api/common/monitor/monitor-route-change-page';
import { MonitorRouteChangesPage } from '@api/common/monitor/monitor-route-changes-page';
import { MonitorRouteDetailsPage } from '@api/common/monitor/monitor-route-details-page';
import { MonitorRouteInfoPage } from '@api/common/monitor/monitor-route-info-page';
import { MonitorRouteMapPage } from '@api/common/monitor/monitor-route-map-page';
import { ApiResponse } from '@api/custom/api-response';
import { createAction } from '@ngrx/store';
import { props } from '@ngrx/store';

export const actionMonitorAdmin = createAction(
  '[Monitor] Admin',
  props<{ admin: boolean }>()
);

export const actionMonitorGroupsPageInit = createAction(
  '[MonitorGroupsPage] Init'
);

export const actionMonitorGroupsPageLoaded = createAction(
  '[MonitorGroupsPage] Loaded',
  props<{ response: ApiResponse<MonitorGroupsPage> }>()
);

export const actionMonitorNavigateGroup = createAction(
  '[MonitorGroupsPage] Navigate to group',
  props<{ groupName: string; groupDescription: string }>()
);

export const actionMonitorGroupDeleteInit = createAction(
  '[MonitorAdminGroupDeletePage] Init'
);

export const actionMonitorGroupDeleteLoaded = createAction(
  '[MonitorAdminGroupDeletePage] Loaded',
  props<{ response: ApiResponse<MonitorGroupPage> }>()
);

export const actionMonitorGroupUpdateInit = createAction(
  '[MonitorAdminGroupUpdatePage] Init'
);

export const actionMonitorGroupUpdateLoaded = createAction(
  '[MonitorAdminGroupUpdatePage] Loaded',
  props<{ response: ApiResponse<MonitorGroupPage> }>()
);

export const actionMonitorGroupPageInit = createAction(
  '[MonitorGroupPage] Init'
);

export const actionMonitorGroupPageLoaded = createAction(
  '[MonitorGroupPage] Loaded',
  props<{ response: ApiResponse<MonitorGroupPage> }>()
);

export const actionMonitorGroupChangesPageInit = createAction(
  '[MonitorGroupChangesPage] Init'
);

export const actionMonitorGroupChangesPageIndex = createAction(
  '[MonitorGroupChangesPage] Page index',
  props<{ pageIndex: number }>()
);

export const actionMonitorGroupChangesPageLoaded = createAction(
  '[MonitorGroupChangesPage] Loaded',
  props<{ response: ApiResponse<MonitorGroupChangesPage> }>()
);

export const actionMonitorRouteDetailsPageInit = createAction(
  '[MonitorRouteDetailsPage] Init'
);

export const actionMonitorRouteDetailsPageLoaded = createAction(
  '[MonitorRouteDetailsPage] Loaded',
  props<{ response: ApiResponse<MonitorRouteDetailsPage> }>()
);

export const actionMonitorRouteMapPageInit = createAction(
  '[MonitorRouteMapPage] Init'
);

export const actionMonitorRouteMapPageLoaded = createAction(
  '[MonitorRouteMapPage] Loaded',
  props<{ response: ApiResponse<MonitorRouteMapPage> }>()
);

export const actionMonitorRouteChangesPageInit = createAction(
  '[MonitorRouteChangesPage] Init'
);

export const actionMonitorRouteChangesPageIndex = createAction(
  '[MonitorRouteChangesPage] Page index',
  props<{ pageIndex: number }>()
);

export const actionMonitorRouteChangesPageLoaded = createAction(
  '[MonitorRouteChangesPage] Loaded',
  props<{ response: ApiResponse<MonitorRouteChangesPage> }>()
);

export const actionMonitorRouteChangePageInit = createAction(
  '[MonitorRouteChangePage] Init'
);

export const actionMonitorRouteChangePageLoaded = createAction(
  '[MonitorRouteChangePage] Loaded',
  props<{ response: ApiResponse<MonitorRouteChangePage> }>()
);

export const actionMonitorRouteMapMode = createAction(
  '[Monitor] Map mode',
  props<{ mode: string }>()
);

export const actionMonitorRouteMapFocus = createAction(
  '[Monitor] Focus',
  props<{ bounds: Bounds }>()
);

export const actionMonitorRouteMapReferenceVisible = createAction(
  '[Monitor] Map reference visible',
  props<{ visible: boolean }>()
);

export const actionMonitorRouteMapOkVisible = createAction(
  '[Monitor] Map ok visible',
  props<{ visible: boolean }>()
);

export const actionMonitorRouteMapNokVisible = createAction(
  '[Monitor] Map nok visible',
  props<{ visible: boolean }>()
);

export const actionMonitorRouteMapOsmRelationVisible = createAction(
  '[Monitor] Map osm relation visible',
  props<{ visible: boolean }>()
);

export const actionMonitorGroupAdd = createAction(
  '[MonitorAdminGroupAddPage] Add group',
  props<{ group: MonitorGroup }>()
);

export const actionMonitorRouteInfo = createAction(
  '[Monitor] Route info',
  props<{ routeId: number }>()
);

export const actionMonitorRouteAdd = createAction(
  '[Monitor] Route add',
  props<{ add: MonitorRouteAdd }>()
);

export const actionMonitorRouteDeletePageInit = createAction(
  '[Monitor] Route delete init'
);

export const actionMonitorRouteDelete = createAction('[Monitor] Route delete');

export const actionMonitorRouteInfoLoaded = createAction(
  '[Monitor] Route info loaded',
  props<{ response: ApiResponse<MonitorRouteInfoPage> }>()
);

export const actionMonitorGroupDelete = createAction(
  '[MonitorAdminGroupDeletePage] Delete',
  props<{ groupName: string }>()
);

export const actionMonitorGroupUpdate = createAction(
  '[MonitorAdminGroupUpdatePage] Update',
  props<{ group: MonitorGroup }>()
);

export const actionMonitorChangesPageInit = createAction(
  '[MonitorChangesPage] Init'
);

export const actionMonitorChangesPageIndex = createAction(
  '[MonitorChangesPage] Page index',
  props<{ pageIndex: number }>()
);

export const actionMonitorChangesPageLoaded = createAction(
  '[MonitorChangesPage] Loaded',
  props<{ response: ApiResponse<MonitorChangesPage> }>()
);
