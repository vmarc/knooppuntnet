import {createSelector} from '@ngrx/store';
import {selectLongDistanceState} from '../core.state';
import {selectPreferencesImpact} from '../preferences/preferences.selectors';
import {LongDistanceState} from './long-distance.state';

export const selectLongDistanceRoutes = createSelector(
  selectLongDistanceState,
  (state: LongDistanceState) => state.routes
);

export const selectLongDistanceRouteDetails = createSelector(
  selectLongDistanceState,
  (state: LongDistanceState) => state.details
);

export const selectLongDistanceRouteMap = createSelector(
  selectLongDistanceState,
  (state: LongDistanceState) => state.map
);

export const selectLongDistanceRouteMapMode = createSelector(
  selectLongDistanceState,
  (state: LongDistanceState) => state.mapMode
);

export const selectLongDistanceRouteMapBounds = createSelector(
  selectLongDistanceState,
  (state: LongDistanceState) => state.map.result.bounds
);

export const selectLongDistanceRouteMapOsmSegments = createSelector(
  selectLongDistanceState,
  (state: LongDistanceState) => {
    if (state.map && state.map.result && state.map.result.osmSegments) {
      return state.map.result.osmSegments;
    }
    return [];
  }
);

export const selectLongDistanceRouteMapOsmSegmentCount = createSelector(
  selectLongDistanceState,
  (state: LongDistanceState) => state.map?.result?.osmSegments?.length ?? 0
);

export const selectLongDistanceRouteMapNokSegments = createSelector(
  selectLongDistanceState,
  (state: LongDistanceState) => state.map?.result?.nokSegments ?? []
);

export const selectLongDistanceRouteMapNokSegmentsCount = createSelector(
  selectLongDistanceState,
  (state: LongDistanceState) => state.map?.result?.nokSegments?.length ?? 0
);

export const selectLongDistanceRouteChanges = createSelector(
  selectLongDistanceState,
  (state: LongDistanceState) => state.changes
);

export const selectLongDistanceRouteChange = createSelector(
  selectLongDistanceState,
  (state: LongDistanceState) => state.change
);

export const selectLongDistanceRouteChangesFiltered = createSelector(
  selectLongDistanceState,
  selectPreferencesImpact,
  (state: LongDistanceState, impact) => {
    const changes = state.changes?.result?.changes ?? [];
    return changes.filter( change => {
      if (impact) {
        return change.happy || change.investigate;
      }
      return true;
    });
  }
);

export const selectLongDistanceRouteId = createSelector(
  selectLongDistanceState,
  (state: LongDistanceState) => state.routeId
);

export const selectLongDistanceRouteName = createSelector(
  selectLongDistanceState,
  (state: LongDistanceState) => state.routeName
);

export const selectLongDistanceRouteMapGpxVisible = createSelector(
  selectLongDistanceState,
  (state: LongDistanceState) => state.mapGpxVisible
);

export const selectLongDistanceRouteMapGpxOkVisible = createSelector(
  selectLongDistanceState,
  (state: LongDistanceState) => state.mapGpxOkVisible
);

export const selectLongDistanceRouteMapGpxNokVisible = createSelector(
  selectLongDistanceState,
  (state: LongDistanceState) => state.mapGpxNokVisible
);

export const selectLongDistanceRouteMapOsmRelationVisible = createSelector(
  selectLongDistanceState,
  (state: LongDistanceState) => state.mapOsmRelationVisible
);

export const selectLongDistanceRouteMapGpxEnabled = createSelector(
  selectLongDistanceState,
  (state: LongDistanceState) => !!state.map?.result?.gpxGeometry
);

export const selectLongDistanceRouteMapGpxOkEnabled = createSelector(
  selectLongDistanceState,
  (state: LongDistanceState) => state.mapMode === 'comparison' && !!state.map?.result?.okGeometry
);

export const selectLongDistanceRouteMapGpxNokEnabled = createSelector(
  selectLongDistanceState,
  (state: LongDistanceState) => state.mapMode === 'comparison' && (state.map?.result?.nokSegments?.length ?? 0) > 0
);

export const selectLongDistanceRouteMapOsmRelationEnabled = createSelector(
  selectLongDistanceState,
  (state: LongDistanceState) => (state.map?.result?.osmSegments?.length ?? 0) > 0
);
