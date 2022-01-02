import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
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

export const selectNetworkChangesPage = createSelector(
  selectNetworkState,
  (state: NetworkState) => state.changesPage
);

export const selectNetworkChangesParameters = createSelector(
  selectNetworkState,
  (state: NetworkState) => state.changesParameters
);

export const selectNetworkChangesParametersImpact = createSelector(
  selectNetworkChangesParameters,
  (changesParameters: ChangesParameters) => changesParameters?.impact ?? true
);

export const selectNetworkChangesParametersItemsPerPage = createSelector(
  selectNetworkChangesParameters,
  (changesParameters: ChangesParameters) =>
    changesParameters?.itemsPerPage ?? 10
);

export const selectNetworkChangesParametersPageIndex = createSelector(
  selectNetworkChangesParameters,
  (changesParameters: ChangesParameters) => changesParameters?.pageIndex ?? 0
);

export const selectNetworkChangesFilterOptions = createSelector(
  selectNetworkState,
  (state: NetworkState) => state.changesPage?.result?.filterOptions
);
