import { LocationSummary } from '@api/common/location/location-summary';
import { createFeatureSelector } from '@ngrx/store';
import { createSelector } from '@ngrx/store';
import { LocationRootState } from './location.state';
import { locationFeatureKey } from './location.state';
import { LocationState } from './location.state';

export const selectLocationState = createFeatureSelector<
  LocationRootState,
  LocationState
>(locationFeatureKey);

export const selectLocationKey = createSelector(
  selectLocationState,
  (state: LocationState) => state.locationKey
);

export const selectLocationSummary = createSelector(
  selectLocationState,
  (state: LocationState) => state.locationSummary
);

export const selectLocationNodeCount = createSelector(
  selectLocationSummary,
  (summary: LocationSummary) => summary?.nodeCount
);

export const selectLocationRouteCount = createSelector(
  selectLocationSummary,
  (summary: LocationSummary) => summary?.routeCount
);

export const selectLocationFactCount = createSelector(
  selectLocationSummary,
  (summary: LocationSummary) => summary?.factCount
);

export const selectLocationChangeCount = createSelector(
  selectLocationSummary,
  (summary: LocationSummary) => summary?.changeCount
);

export const selectLocationNodesType = createSelector(
  selectLocationState,
  (state: LocationState) => state.nodesPageType
);

export const selectLocationNodesPageIndex = createSelector(
  selectLocationState,
  (state: LocationState) => state.nodesPageIndex
);

export const selectLocationNodesPage = createSelector(
  selectLocationState,
  (state: LocationState) => state.nodesPage
);

export const selectLocationRoutesPage = createSelector(
  selectLocationState,
  (state: LocationState) => state.routesPage
);

export const selectLocationFactsPage = createSelector(
  selectLocationState,
  (state: LocationState) => state.factsPage
);

export const selectLocationMapPage = createSelector(
  selectLocationState,
  (state: LocationState) => state.mapPage
);

export const selectLocationChangesPage = createSelector(
  selectLocationState,
  (state: LocationState) => state.changesPage
);

export const selectLocationEditPage = createSelector(
  selectLocationState,
  (state: LocationState) => state.editPage
);
