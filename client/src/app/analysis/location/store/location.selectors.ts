import {createFeatureSelector} from '@ngrx/store';
import {createSelector} from '@ngrx/store';
import {LocationRootState} from './location.state';
import {locationFeatureKey} from './location.state';
import {LocationState} from './location.state';

export const selectLocationState = createFeatureSelector<LocationRootState, LocationState>(locationFeatureKey);

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
