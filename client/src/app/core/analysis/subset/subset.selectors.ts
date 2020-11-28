import {createSelector} from '@ngrx/store';
import {selectSubsetState} from '../../core.state';
import {SubsetState} from './subset.state';

export const selectSubsetNetworks = createSelector(
  selectSubsetState,
  (state: SubsetState) => state.networks
);

export const selectSubsetFacts = createSelector(
  selectSubsetState,
  (state: SubsetState) => state.facts
);

export const selectSubsetOrphanNodes = createSelector(
  selectSubsetState,
  (state: SubsetState) => state.orphanNodes
);

export const selectSubsetOrphanRoutes = createSelector(
  selectSubsetState,
  (state: SubsetState) => state.orphanRoutes
);

export const selectSubsetMap = createSelector(
  selectSubsetState,
  (state: SubsetState) => state.map
);

export const selectSubsetChanges = createSelector(
  selectSubsetState,
  (state: SubsetState) => state.changes
);
