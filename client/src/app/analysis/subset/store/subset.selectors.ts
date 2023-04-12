import { ChangesParameters } from '@api/common/changes/filter';
import { SubsetInfo } from '@api/common/subset';
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

export const selectSubsetNetworkType = createSelector(
  selectSubsetInfo,
  (subsetInfo: SubsetInfo) => subsetInfo.networkType
);

export const selectSubsetFact = createSelector(
  selectSubsetState,
  (state: SubsetState) => state.subsetFact
);

export const selectSubsetNetworksPage = createSelector(
  selectSubsetState,
  (state: SubsetState) => state.networksPage
);

export const selectSubsetFactsPage = createSelector(
  selectSubsetState,
  (state: SubsetState) => state.factsPage
);

export const selectSubsetFactDetailsPage = createSelector(
  selectSubsetState,
  (state: SubsetState) => state.factDetailsPage
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

export const selectSubsetChangesPageImpact = createSelector(
  selectSubsetChangesParameters,
  (changesParameters: ChangesParameters) => changesParameters.impact
);

export const selectSubsetChangesPageSize = createSelector(
  selectSubsetChangesParameters,
  (changesParameters: ChangesParameters) => changesParameters.pageSize
);

export const selectSubsetChangesPageIndex = createSelector(
  selectSubsetChangesParameters,
  (changesParameters: ChangesParameters) => changesParameters.pageIndex
);
