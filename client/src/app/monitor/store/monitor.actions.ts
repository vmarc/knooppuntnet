import {BoundsI} from '@api/common/bounds-i';
import {LongdistanceRouteChangePage} from '@api/common/monitor/longdistance-route-change-page';
import {LongdistanceRouteChangesPage} from '@api/common/monitor/longdistance-route-changes-page';
import {LongdistanceRouteDetailsPage} from '@api/common/monitor/longdistance-route-details-page';
import {LongdistanceRouteMapPage} from '@api/common/monitor/longdistance-route-map-page';
import {LongdistanceRoutesPage} from '@api/common/monitor/longdistance-routes-page';
import {MonitorAdminGroupPage} from '@api/common/monitor/monitor-admin-group-page';
import {MonitorRouteChangePage} from '@api/common/monitor/monitor-route-change-page';
import {MonitorRouteChangesPage} from '@api/common/monitor/monitor-route-changes-page';
import {MonitorRouteDetailsPage} from '@api/common/monitor/monitor-route-details-page';
import {MonitorGroup} from '@api/common/monitor/monitor-group';
import {MonitorRouteMapPage} from '@api/common/monitor/monitor-route-map-page';
import {MonitorGroupPage} from '@api/common/monitor/monitor-group-page';
import {RouteGroupsPage} from '@api/common/monitor/route-groups-page';
import {ApiResponse} from '@api/custom/api-response';
import {createAction} from '@ngrx/store';
import {props} from '@ngrx/store';

export const actionMonitorAdmin = createAction(
  '[Monitor] Admin',
  props<{ admin: boolean }>()
);

export const actionMonitorInit = createAction(
  '[Monitor] Init'
);

export const actionMonitorLoaded = createAction(
  '[Monitor] Loaded',
  props<{ response: ApiResponse<RouteGroupsPage> }>()
);

export const actionMonitorGroupDeleteInit = createAction(
  '[Monitor group delete] Init'
);

export const actionMonitorGroupDeleteLoaded = createAction(
  '[Monitor group delete] Loaded',
  props<{ response: ApiResponse<MonitorAdminGroupPage> }>()
);

export const actionMonitorGroupUpdateInit = createAction(
  '[Monitor group update] Init'
);

export const actionMonitorGroupUpdateLoaded = createAction(
  '[Monitor group update] Loaded',
  props<{ response: ApiResponse<MonitorAdminGroupPage> }>()
);

export const actionMonitorRoutesInit = createAction(
  '[Monitor routes] Init'
);

export const actionMonitorRoutesLoaded = createAction(
  '[Monitor routes] Loaded',
  props<{ response: ApiResponse<MonitorGroupPage> }>()
);

export const actionMonitorRouteDetailsInit = createAction(
  '[Monitor route details] Init'
);

export const actionMonitorRouteDetailsLoaded = createAction(
  '[Monitor route details] Loaded',
  props<{ response: ApiResponse<MonitorRouteDetailsPage> }>()
);

export const actionMonitorRouteMapInit = createAction(
  '[Monitor route map] Init'
);

export const actionMonitorRouteMapLoaded = createAction(
  '[Monitor route map] Loaded',
  props<{ response: ApiResponse<MonitorRouteMapPage> }>()
);

export const actionMonitorRouteChangesInit = createAction(
  '[Monitor route changes] Init'
);

export const actionMonitorRouteChangesLoaded = createAction(
  '[Monitor route changes] Loaded',
  props<{ response: ApiResponse<MonitorRouteChangesPage> }>()
);

export const actionMonitorRouteChangeInit = createAction(
  '[Monitor route change] Init'
);

export const actionMonitorRouteChangeLoaded = createAction(
  '[Monitor route change] Loaded',
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

export const actionMonitorAddRouteGroup = createAction(
  '[Monitor] Add route group',
  props<{ group: MonitorGroup }>()
);

export const actionMonitorDeleteRouteGroup = createAction(
  '[Monitor] Delete route group',
  props<{ groupName: string }>()
);

export const actionMonitorUpdateRouteGroup = createAction(
  '[Monitor] Update route group',
  props<{ group: MonitorGroup }>()
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
