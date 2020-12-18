import {BoundsI} from '@api/common/bounds-i';
import {MonitorAdminRouteGroupPage} from '@api/common/monitor/monitor-admin-route-group-page';
import {MonitorRouteChangePage} from '@api/common/monitor/monitor-route-change-page';
import {MonitorRouteChangesPage} from '@api/common/monitor/monitor-route-changes-page';
import {MonitorRouteDetailsPage} from '@api/common/monitor/monitor-route-details-page';
import {MonitorRouteGroup} from '@api/common/monitor/monitor-route-group';
import {MonitorRouteMapPage} from '@api/common/monitor/monitor-route-map-page';
import {MonitorRoutesPage} from '@api/common/monitor/monitor-routes-page';
import {RouteGroupsPage} from '@api/common/monitor/route-groups-page';
import {ApiResponse} from '@api/custom/api-response';
import {createAction} from '@ngrx/store';
import {props} from '@ngrx/store';

export const actionMonitorAdmin = createAction(
  '[Monitor] Admin',
  props<{ admin: boolean }>()
);

export const actionMonitorLoaded = createAction(
  '[Monitor] Monitor loaded',
  props<{ response: ApiResponse<RouteGroupsPage> }>()
);

export const actionMonitorGroupDeleteLoaded = createAction(
  '[Monitor] Monitor group delete loaded',
  props<{ response: ApiResponse<MonitorAdminRouteGroupPage> }>()
);

export const actionMonitorRoutesLoaded = createAction(
  '[Monitor] Routes loaded',
  props<{ response: ApiResponse<MonitorRoutesPage> }>()
);

export const actionMonitorRouteDetailsLoaded = createAction(
  '[Monitor] Route details loaded',
  props<{ response: ApiResponse<MonitorRouteDetailsPage> }>()
);

export const actionMonitorRouteMapLoaded = createAction(
  '[Monitor] Route map loaded',
  props<{ response: ApiResponse<MonitorRouteMapPage> }>()
);

export const actionMonitorRouteChangesLoaded = createAction(
  '[Monitor] Route changes loaded',
  props<{ response: ApiResponse<MonitorRouteChangesPage> }>()
);

export const actionMonitorRouteChangeLoaded = createAction(
  '[Monitor] Route change loaded',
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
  props<{ group: MonitorRouteGroup }>()
);

export const actionMonitorDeleteRouteGroup = createAction(
  '[Monitor] Delete route group',
  props<{ groupName: string }>()
);
