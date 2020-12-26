import {createFeatureSelector} from '@ngrx/store';
import {createSelector} from '@ngrx/store';
import {NetworkRootState} from './network.state';
import {networkFeatureKey} from './network.state';
import {NetworkState} from './network.state';

export const selectNetworkState = createFeatureSelector<NetworkRootState, NetworkState>(networkFeatureKey);

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
