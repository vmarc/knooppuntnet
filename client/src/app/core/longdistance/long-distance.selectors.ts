import {createSelector} from '@ngrx/store';
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

export const selectLongDistanceRouteMapOsmSegments = createSelector(
  selectLongDistanceState,
  (state: LongDistanceState) => {
    if (state.map && state.map.result && state.map.result.osmSegments) {
      return state.map.result.osmSegments;
    }
    return [];
  }
);

export const selectLongDistanceRouteMapNokSegments = createSelector(
  selectLongDistanceState,
  (state: LongDistanceState) => {
    if (state.map && state.map.result && state.map.result.nokSegments) {
      return state.map.result.nokSegments;
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
