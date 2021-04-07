import { createFeatureSelector } from '@ngrx/store';
import { createSelector } from '@ngrx/store';
import { SubsetRootState } from './subset.state';
import { subsetFeatureKey } from './subset.state';
import { SubsetState } from './subset.state';

export const selectSubsetState = createFeatureSelector<
  SubsetRootState,
  SubsetState
>(subsetFeatureKey);

export const selectSubsetNetworksPage = createSelector(
  selectSubsetState,
  (state: SubsetState) => state.networksPage
);

export const selectSubsetFactsPage = createSelector(
  selectSubsetState,
  (state: SubsetState) => state.factsPage
);

export const selectSubsetOrphanNodesPage = createSelector(
  selectSubsetState,
  (state: SubsetState) => state.orphanNodesPage
);

export const selectSubsetOrphanRoutesPage = createSelector(
  selectSubsetState,
  (state: SubsetState) => state.orphanRoutesPage
);

export const selectSubsetMapPage = createSelector(
  selectSubsetState,
  (state: SubsetState) => state.mapPage
);

export const selectSubsetChangesPage = createSelector(
  selectSubsetState,
  (state: SubsetState) => state.changesPage
);
