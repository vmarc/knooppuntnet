import {createSelector} from '@ngrx/store';
import {LongDistanceRouteSegment} from '../../kpn/api/common/longdistance/long-distance-route-segment';
import {selectLongDistanceState} from '../core.state';
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
  (state: LongDistanceState) => {
    const segments = state.map?.result?.osmSegments;
    if (segments) {
      // @ts-ignore
      return (segments as LongDistanceRouteSegment[]).length;
    }
    return 0;
  }
);

export const selectLongDistanceRouteMapNokSegments = createSelector(
  selectLongDistanceState,
  (state: LongDistanceState) => {
    return state.map?.result?.nokSegments ?? [];
  }
);

export const selectLongDistanceRouteMapNokSegmentsCount = createSelector(
  selectLongDistanceState,
  (state: LongDistanceState) => {
    const segments = state.map?.result?.nokSegments;
    if (segments) {
      // @ts-ignore
      return (segments as LongDistanceRouteNokSegment[]).length;
    }
    return [];
  }
);

export const selectLongDistanceRouteChanges = createSelector(
  selectLongDistanceState,
  (state: LongDistanceState) => state.changes
);

export const selectLongDistanceRouteId = createSelector(
  selectLongDistanceState,
  (state: LongDistanceState) => state.routeId
);

export const selectLongDistanceRouteName = createSelector(
  selectLongDistanceState,
  (state: LongDistanceState) => state.routeName
);

export const selectLongDistanceRouteMapFocus = createSelector(
  selectLongDistanceState,
  (state: LongDistanceState) => state.mapFocus
);

export const selectLongDistanceRouteMapFocusNokSegmentId = createSelector(
  selectLongDistanceState,
  (state: LongDistanceState) => state.mapFocusNokSegmentId
);
