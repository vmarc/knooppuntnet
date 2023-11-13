import { ChangesParameters } from '@api/common/changes/filter';
import { createFeatureSelector } from '@ngrx/store';
import { createSelector } from '@ngrx/store';
import { networkFeatureKey } from './network.state';
import { NetworkState } from './network.state';

export const selectNetworkState =
  createFeatureSelector<NetworkState>(networkFeatureKey);

export const selectNetworkId = createSelector(
  selectNetworkState,
  (state: NetworkState) => state.networkId
);

export const selectNetworkSummary = createSelector(
  selectNetworkState,
  (state: NetworkState) => state.summary
);

export const selectNetworkDetailsPage = createSelector(
  selectNetworkState,
  (state: NetworkState) => state.detailsPage
);

export const selectNetworkNodesPage = createSelector(
  selectNetworkState,
  (state: NetworkState) => state.nodesPage
);

export const selectNetworkRoutesPage = createSelector(
  selectNetworkState,
  (state: NetworkState) => state.routesPage
);

export const selectNetworkFactsPage = createSelector(
  selectNetworkState,
  (state: NetworkState) => state.factsPage
);

export const selectNetworkMapPage = createSelector(
  selectNetworkState,
  (state: NetworkState) => state.mapPage
);

export const selectNetworkMapPositionFromUrl = createSelector(
  selectNetworkState,
  (state: NetworkState) => state.mapPositionFromUrl
);

export const selectNetworkChangesPage = createSelector(
  selectNetworkState,
  (state: NetworkState) => state.changesPage
);

export const selectNetworkChangesParameters = createSelector(
  selectNetworkState,
  (state: NetworkState) => state.changesParameters
);

export const selectNetworkChangesImpact = createSelector(
  selectNetworkChangesParameters,
  (changesParameters: ChangesParameters) => changesParameters.impact
);

export const selectNetworkChangesPageSize = createSelector(
  selectNetworkChangesParameters,
  (changesParameters: ChangesParameters) => changesParameters.pageSize
);

export const selectNetworkChangesPageIndex = createSelector(
  selectNetworkChangesParameters,
  (changesParameters: ChangesParameters) => changesParameters.pageIndex
);

export const selectNetworkChangesFilterOptions = createSelector(
  selectNetworkState,
  (state: NetworkState) => state.changesPage?.result?.filterOptions
);
