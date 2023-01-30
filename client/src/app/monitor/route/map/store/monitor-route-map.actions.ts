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
  '[MonitorRouteMapPage] Init'
);

export const actionMonitorRouteMapPageLoad = createAction(
  '[MonitorRouteMapPage] Load',
  props<{ groupName: string; routeName: string; relationId: number }>()
);

export const actionMonitorRouteMapPageDestroy = createAction(
  '[MonitorRouteMapPage] Destroy'
);

export const actionMonitorRouteMapPageLoaded = createAction(
  '[MonitorRouteMapPage] Loaded',
  props<{
    response: ApiResponse<MonitorRouteMapPage>;
    queryParams: Params;
  }>()
);

export const actionMonitorRouteMapMode = createAction(
  '[MonitorRouteMapPage] Map mode',
  props<{ mapMode: MonitorMapMode }>()
);

export const actionMonitorRouteMapPositionChanged = createAction(
  '[MonitorRouteMapPage] Map position changed',
  props<{ mapPosition: MapPosition }>()
);

export const actionMonitorRouteMapSelectDeviation = createAction(
  '[MonitorRouteMapPage] Map select deviation',
  props<MonitorRouteDeviation | null>()
);

export const actionMonitorRouteMapSelectOsmSegment = createAction(
  '[MonitorRouteMapPage] Map select OSM segment',
  props<MonitorRouteSegment | null>()
);

export const actionMonitorRouteMapFocus = createAction(
  '[MonitorRouteMapPage] Focus',
  props<Bounds>()
);

export const actionMonitorRouteMapReferenceVisible = createAction(
  '[MonitorRouteMapPage] Map reference visible',
  props<{ visible: boolean }>()
);

export const actionMonitorRouteMapMatchesVisible = createAction(
  '[MonitorRouteMapPage] Map matches visible',
  props<{ visible: boolean }>()
);

export const actionMonitorRouteMapDeviationsVisible = createAction(
  '[MonitorRouteMapPage] Map deviations visible',
  props<{ visible: boolean }>()
);

export const actionMonitorRouteMapOsmRelationVisible = createAction(
  '[MonitorRouteMapPage] Map osm relation visible',
  props<{ visible: boolean }>()
);

export const actionMonitorRouteMapJosmLoadRouteRelation = createAction(
  '[MonitorRouteMapPage] Map josm load route relation'
);

export const actionMonitorRouteMapJosmZoomToFitRoute = createAction(
  '[MonitorRouteMapPage] Map josm zoom to fit route'
);

export const actionMonitorRouteMapJosmZoomToSelectedDeviation = createAction(
  '[MonitorRouteMapPage] Map josm zoom to fit selected deviation'
);

export const actionMonitorRouteMapJosmZoomToSelectedOsmSegment = createAction(
  '[MonitorRouteMapPage] Map josm zoom to fit selected OSM segment'
);

export const actionMonitorRouteMapSelectSubRelation = createAction(
  '[MonitorRouteMapPage] Map select sub relation',
  props<MonitorRouteSubRelation>()
);
