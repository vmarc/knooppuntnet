import { MonitorRouteDeviation } from '@api/common/monitor/monitor-route-deviation';
import { MonitorRouteMapPage } from '@api/common/monitor/monitor-route-map-page';
import { createFeatureSelector } from '@ngrx/store';
import { createSelector } from '@ngrx/store';
import { MonitorMapMode } from '../monitor-map-mode';
import { monitorRouteMapFeatureKey } from './monitor-route-map.state';
import { MonitorRouteMapState } from './monitor-route-map.state';

export const selectMonitorRouteMapState =
  createFeatureSelector<MonitorRouteMapState>(monitorRouteMapFeatureKey);

export const selectMonitorRouteMapPage = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => state.page
);

export const selectMonitorRouteMapReferenceType = createSelector(
  selectMonitorRouteMapPage,
  (page: MonitorRouteMapPage) => page?.reference?.referenceType
);

export const selectMonitorRouteMapMode = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => state.mode
);

export const selectMonitorRouteMapSubRelations = createSelector(
  selectMonitorRouteMapPage,
  (page: MonitorRouteMapPage) => page?.subRelations
);

export const selectMonitorRouteMapPreviousSubRelation = createSelector(
  selectMonitorRouteMapPage,
  (page: MonitorRouteMapPage) => page?.previousSubRelation
);

export const selectMonitorRouteMapNextSubRelation = createSelector(
  selectMonitorRouteMapPage,
  (page: MonitorRouteMapPage) => page?.nextSubRelation
);

export const selectMonitorRouteMapBounds = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => state.page.bounds
);

export const selectMonitorRouteMapOsmSegments = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => {
    if (state.page && state.page.osmSegments) {
      return state.page.osmSegments;
    }
    return [];
  }
);

export const selectMonitorRouteMapOsmSegmentCount = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => state.page?.osmSegments?.length ?? 0
);

export const selectMonitorRouteMapModeSelectionEnabled = createSelector(
  selectMonitorRouteMapOsmSegmentCount,
  (osmSegmentCount: number) => osmSegmentCount > 1
);

export const selectMonitorRouteMapDeviations = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => state.page?.deviations ?? []
);

export const selectMonitorRouteMapSelectedDeviation = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => state.selectedDeviation
);

export const selectMonitorRouteMapSelectedDeviationId = createSelector(
  selectMonitorRouteMapSelectedDeviation,
  (deviation: MonitorRouteDeviation | undefined) => deviation?.id
);

export const selectMonitorRouteMapSelectedDeviationDisabled = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) =>
    state.mode !== MonitorMapMode.comparison || !state.selectedDeviation
);

export const selectMonitorRouteMapSelectedOsmSegment = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => state.selectedOsmSegment
);

export const selectMonitorRouteMapSelectedOsmSegmentId = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => state.selectedOsmSegment?.id
);

export const selectMonitorRouteMapSelectedOsmSegmentDisabled = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) =>
    state.mode !== MonitorMapMode.osmSegments || !state.selectedOsmSegment
);

export const selectMonitorRouteMapReferenceVisible = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => state.referenceVisible
);

export const selectMonitorRouteMapMatchesVisible = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => state.matchesVisible
);

export const selectMonitorRouteMapDeviationsVisible = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => state.deviationsVisible
);

export const selectMonitorRouteMapOsmRelationVisible = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => state.osmRelationVisible
);

export const selectMonitorRouteMapOsmRelationAvailable = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => state.osmRelationAvailable
);

export const selectMonitorRouteMapOsmRelationEmpty = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => state.osmRelationEmpty
);

export const selectMonitorRouteMapReferenceEnabled = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => !!state.page?.reference?.geoJson
);

export const selectMonitorRouteMapMatchesEnabled = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) =>
    state.mode === 'comparison' && !!state.page?.matchesGeoJson
);

export const selectMonitorRouteMapDeviationsEnabled = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) =>
    state.mode === MonitorMapMode.comparison &&
    (state.page?.deviations?.length ?? 0) > 0
);

export const selectMonitorRouteMapOsmRelationEnabled = createSelector(
  selectMonitorRouteMapState,
  (state: MonitorRouteMapState) => (state.page?.osmSegments?.length ?? 0) > 0
);
