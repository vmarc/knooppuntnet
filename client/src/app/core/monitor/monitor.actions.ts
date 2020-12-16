import {createAction} from '@ngrx/store';
import {props} from '@ngrx/store';
import {BoundsI} from '../../kpn/api/common/bounds-i';
import {MonitorRouteChangePage} from '../../kpn/api/common/monitor/monitor-route-change-page';
import {MonitorRouteChangesPage} from '../../kpn/api/common/monitor/monitor-route-changes-page';
import {MonitorRouteDetailsPage} from '../../kpn/api/common/monitor/monitor-route-details-page';
import {MonitorRouteMapPage} from '../../kpn/api/common/monitor/monitor-route-map-page';
import {MonitorRoutesPage} from '../../kpn/api/common/monitor/monitor-routes-page';
import {ApiResponse} from '../../kpn/api/custom/api-response';

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
