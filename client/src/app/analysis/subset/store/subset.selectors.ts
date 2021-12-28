import { createFeatureSelector } from '@ngrx/store';
import { createSelector } from '@ngrx/store';
import { subsetFeatureKey } from './subset.state';
import { SubsetState } from './subset.state';

export const selectSubsetState =
  createFeatureSelector<SubsetState>(subsetFeatureKey);

export const selectSubset = createSelector(
  selectSubsetState,
  (state: SubsetState) => state.subset
);

export const selectSubsetInfo = createSelector(
  selectSubsetState,
  (state: SubsetState) => state.subsetInfo
);

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

export const selectSubsetChangesFilterOptions = createSelector(
  selectSubsetState,
  (state: SubsetState) => state.changesPage?.result?.filterOptions
);

export const selectSubsetChangesParameters = createSelector(
  selectSubsetState,
  (state: SubsetState) => state.changesParameters
);
