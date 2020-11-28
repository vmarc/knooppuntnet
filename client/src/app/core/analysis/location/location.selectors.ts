import {createSelector} from '@ngrx/store';
import {selectLocationState} from '../../core.state';
import {LocationState} from './location.state';

export const selectLocationNodes = createSelector(
  selectLocationState,
  (state: LocationState) => state.nodes
);

export const selectLocationRoutes = createSelector(
  selectLocationState,
  (state: LocationState) => state.routes
);

export const selectLocationFacts = createSelector(
  selectLocationState,
  (state: LocationState) => state.facts
);

export const selectLocationMap = createSelector(
  selectLocationState,
  (state: LocationState) => state.map
);

export const selectLocationChanges = createSelector(
  selectLocationState,
  (state: LocationState) => state.changes
);

export const selectLocationEdit = createSelector(
  selectLocationState,
  (state: LocationState) => state.edit
);
