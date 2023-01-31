import { Params } from '@angular/router';
import { Bounds } from '@api/common/bounds';
import { MonitorRouteDeviation } from '@api/common/monitor/monitor-route-deviation';
import { MonitorRouteMapPage } from '@api/common/monitor/monitor-route-map-page';
import { MonitorRouteSegment } from '@api/common/monitor/monitor-route-segment';
import { MonitorRouteSubRelation } from '@api/common/monitor/monitor-route-sub-relation';
import { ApiResponse } from '@api/custom/api-response';
import { props } from '@ngrx/store';
import { createAction } from '@ngrx/store';
import { MapPosition } from '../../../../components/ol/domain/map-position';
import { MonitorMapMode } from '../monitor-map-mode';

export const actionMonitorRouteMapPageInit = createAction(
  '[MonitorRouteMap] Page init'
);

export const actionMonitorRouteMapPageLoad = createAction(
  '[MonitorRouteMap] Page load',
  props<{ groupName: string; routeName: string; relationId: number }>()
);

export const actionMonitorRouteMapPageDestroy = createAction(
  '[MonitorRouteMap] Page destroy'
);

export const actionMonitorRouteMapPageLoaded = createAction(
  '[MonitorRouteMap] Page loaded',
  props<{
    response: ApiResponse<MonitorRouteMapPage>;
    queryParams: Params;
  }>()
);

export const actionMonitorRouteMapMode = createAction(
  '[MonitorRouteMap] Mode',
  props<{ mapMode: MonitorMapMode }>()
);

export const actionMonitorRouteMapPositionChanged = createAction(
  '[MonitorRouteMap] Position changed',
  props<{ mapPosition: MapPosition }>()
);

export const actionMonitorRouteMapSelectDeviation = createAction(
  '[MonitorRouteMap] Select deviation',
  props<MonitorRouteDeviation | null>()
);

export const actionMonitorRouteMapSelectOsmSegment = createAction(
  '[MonitorRouteMape] Select OSM segment',
  props<MonitorRouteSegment | null>()
);

export const actionMonitorRouteMapFocus = createAction(
  '[MonitorRouteMap] Focus',
  props<Bounds>()
);

export const actionMonitorRouteMapZoomToFitRoute = createAction(
  '[MonitorRouteMap] Zoom to fit route'
);

export const actionMonitorRouteMapReferenceVisible = createAction(
  '[MonitorRouteMap] Reference visible',
  props<{ visible: boolean }>()
);

export const actionMonitorRouteMapMatchesVisible = createAction(
  '[MonitorRouteMap] Matches visible',
  props<{ visible: boolean }>()
);

export const actionMonitorRouteMapDeviationsVisible = createAction(
  '[MonitorRouteMap] Deviations visible',
  props<{ visible: boolean }>()
);

export const actionMonitorRouteMapOsmRelationVisible = createAction(
  '[MonitorRouteMap] Osm relation visible',
  props<{ visible: boolean }>()
);

export const actionMonitorRouteMapJosmLoadRouteRelation = createAction(
  '[MonitorRouteMap] Josm load route relation'
);

export const actionMonitorRouteMapJosmZoomToFitRoute = createAction(
  '[MonitorRouteMap] Josm zoom to fit route'
);

export const actionMonitorRouteMapJosmZoomToSelectedDeviation = createAction(
  '[MonitorRouteMap] Josm zoom to fit selected deviation'
);

export const actionMonitorRouteMapJosmZoomToSelectedOsmSegment = createAction(
  '[MonitorRouteMap] Josm zoom to fit selected OSM segment'
);

export const actionMonitorRouteMapSelectSubRelation = createAction(
  '[MonitorRouteMap] Select sub relation',
  props<MonitorRouteSubRelation>()
);
