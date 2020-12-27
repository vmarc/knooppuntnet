import {BoundsI} from '@api/common/bounds-i';
import {LongdistanceRouteChangePage} from '@api/common/monitor/longdistance-route-change-page';
import {LongdistanceRouteChangesPage} from '@api/common/monitor/longdistance-route-changes-page';
import {LongdistanceRouteDetailsPage} from '@api/common/monitor/longdistance-route-details-page';
import {LongdistanceRouteMapPage} from '@api/common/monitor/longdistance-route-map-page';
import {LongdistanceRoutesPage} from '@api/common/monitor/longdistance-routes-page';
import {MonitorAdminGroupPage} from '@api/common/monitor/monitor-admin-group-page';
import {MonitorChangesPage} from '@api/common/monitor/monitor-changes-page';
import {MonitorGroup} from '@api/common/monitor/monitor-group';
import {MonitorGroupChangesPage} from '@api/common/monitor/monitor-group-changes-page';
import {MonitorGroupPage} from '@api/common/monitor/monitor-group-page';
import {MonitorGroupsPage} from '@api/common/monitor/monitor-groups-page';
import {MonitorRouteChangePage} from '@api/common/monitor/monitor-route-change-page';
import {MonitorRouteChangesPage} from '@api/common/monitor/monitor-route-changes-page';
import {MonitorRouteDetailsPage} from '@api/common/monitor/monitor-route-details-page';
import {MonitorRouteMapPage} from '@api/common/monitor/monitor-route-map-page';
import {ApiResponse} from '@api/custom/api-response';
import {createAction} from '@ngrx/store';
import {props} from '@ngrx/store';

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
  props<{ groupName: string, groupDescription: string }>()
);

export const actionMonitorGroupDeleteInit = createAction(
  '[MonitorAdminGroupDeletePage] Init'
);

export const actionMonitorGroupDeleteLoaded = createAction(
  '[MonitorAdminGroupDeletePage] Loaded',
  props<{ response: ApiResponse<MonitorAdminGroupPage> }>()
);

export const actionMonitorGroupUpdateInit = createAction(
  '[MonitorAdminGroupUpdatePage] Init'
);

export const actionMonitorGroupUpdateLoaded = createAction(
  '[MonitorAdminGroupUpdatePage] Loaded',
  props<{ response: ApiResponse<MonitorAdminGroupPage> }>()
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
  props<{ bounds: BoundsI }>()
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

/*****************************************/

export const actionLongdistanceRoutesInit = createAction(
  '[Longdistance routes] Init'
);

export const actionLongdistanceRoutesLoaded = createAction(
  '[Longdistance routes] Loaded',
  props<{ response: ApiResponse<LongdistanceRoutesPage> }>()
);

export const actionLongdistanceRouteDetailsInit = createAction(
  '[Longdistance route details] Init'
);

export const actionLongdistanceRouteDetailsLoaded = createAction(
  '[Longdistance route details] Loaded',
  props<{ response: ApiResponse<LongdistanceRouteDetailsPage> }>()
);

export const actionLongdistanceRouteMapInit = createAction(
  '[Longdistance route map] Init'
);

export const actionLongdistanceRouteMapLoaded = createAction(
  '[Longdistance route map] Loaded',
  props<{ response: ApiResponse<LongdistanceRouteMapPage> }>()
);

export const actionLongdistanceRouteChangesInit = createAction(
  '[Longdistance route changes] Init'
);

export const actionLongdistanceRouteChangesLoaded = createAction(
  '[Longdistance route changes] Loaded',
  props<{ response: ApiResponse<LongdistanceRouteChangesPage> }>()
);

export const actionLongdistanceRouteChangeInit = createAction(
  '[Longdistance route change] Init'
);

export const actionLongdistanceRouteChangeLoaded = createAction(
  '[Longdistance route change] Loaded',
  props<{ response: ApiResponse<LongdistanceRouteChangePage> }>()
);

export const actionLongdistanceRouteMapMode = createAction(
  '[Longdistance route map] Map mode',
  props<{ mode: string }>()
);

export const actionLongdistanceRouteMapFocus = createAction(
  '[Longdistance route map] Focus',
  props<{ bounds: BoundsI }>()
);

export const actionLongdistanceRouteMapReferenceVisible = createAction(
  '[Longdistance route map] Reference visible',
  props<{ visible: boolean }>()
);

export const actionLongdistanceRouteMapOkVisible = createAction(
  '[Longdistance route map] Ok visible',
  props<{ visible: boolean }>()
);

export const actionLongdistanceRouteMapNokVisible = createAction(
  '[Longdistance route map] Nok visible',
  props<{ visible: boolean }>()
);

export const actionLongdistanceRouteMapOsmRelationVisible = createAction(
  '[Longdistance route map] Osm relation visible',
  props<{ visible: boolean }>()
);
