import { createFeatureSelector } from '@ngrx/store';
import { createSelector } from '@ngrx/store';
import { MonitorMapMode } from '../monitor-map-mode';
import { monitorRouteMapFeatureKey } from './monitor-route-map.state';
import { MonitorRouteMapState } from './monitor-route-map.state';

export const selectMonitorRouteMapState =
  createFeatureSelector<MonitorRouteMapState>(monitorRouteMapFeatureKey);

export const selectMonitorRouteMapPage = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => state.routeMapPage
);

export const selectMonitorRouteMapMode = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => state.mapMode
);

export const selectMonitorRouteMapBounds = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => state.routeMapPage.result.bounds
);

export const selectMonitorRouteMapOsmSegments = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => {
    if (
      state.routeMapPage &&
      state.routeMapPage.result &&
      state.routeMapPage.result.osmSegments
    ) {
      return state.routeMapPage.result.osmSegments;
    }
    return [];
  }
);

export const selectMonitorRouteMapOsmSegmentCount = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) =>
    state.routeMapPage?.result?.osmSegments?.length ?? 0
);

export const selectMonitorRouteMapDeviations = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => state.routeMapPage?.result?.deviations ?? []
);

export const selectMonitorRouteMapSelectedDeviation = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => state.routeMapSelectedDeviation
);

export const selectMonitorRouteMapSelectedDeviationDisabled = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) =>
    state.mapMode !== MonitorMapMode.comparison ||
    !state.routeMapSelectedDeviation
);

export const selectMonitorRouteMapSelectedOsmSegment = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => state.routeMapSelectedOsmSegment
);

export const selectMonitorRouteMapSelectedOsmSegmentId = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => state.routeMapSelectedOsmSegment?.id
);

export const selectMonitorRouteMapSelectedOsmSegmentDisabled = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) =>
    state.mapMode !== MonitorMapMode.osmSegments ||
    !state.routeMapSelectedOsmSegment
);

export const selectMonitorRouteMapReferenceVisible = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => state.mapReferenceVisible
);

export const selectMonitorRouteMapMatchesVisible = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => state.mapMatchesVisible
);

export const selectMonitorRouteMapDeviationsVisible = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => state.mapDeviationsVisible
);

export const selectMonitorRouteMapOsmRelationVisible = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => state.mapOsmRelationVisible
);

export const selectMonitorRouteMapOsmRelationAvailable = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => state.mapOsmRelationAvailable
);

export const selectMonitorRouteMapOsmRelationEmpty = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => state.mapOsmRelationEmpty
);

export const selectMonitorRouteMapReferenceEnabled = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) =>
    !!state.routeMapPage?.result?.reference?.geoJson
);

export const selectMonitorRouteMapMatchesEnabled = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) =>
    state.mapMode === 'comparison' &&
    !!state.routeMapPage?.result?.matchesGeoJson
);

export const selectMonitorRouteMapDeviationsEnabled = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) =>
    state.mapMode === MonitorMapMode.comparison &&
    (state.routeMapPage?.result?.deviations?.length ?? 0) > 0
);

export const selectMonitorRouteMapOsmRelationEnabled = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) =>
    (state.routeMapPage?.result?.osmSegments?.length ?? 0) > 0
);
