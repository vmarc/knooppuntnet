import {createSelector} from '@ngrx/store';
import {selectNetworkState} from '../../core.state';
import {NetworkState} from './network.state';

export const selectNetworkDetails = createSelector(
  selectNetworkState,
  (state: NetworkState) => state.details
);

export const selectNetworkNodes = createSelector(
  selectNetworkState,
  (state: NetworkState) => state.nodes
);

export const selectNetworkRoutes = createSelector(
  selectNetworkState,
  (state: NetworkState) => state.routes
);

export const selectNetworkFacts = createSelector(
  selectNetworkState,
  (state: NetworkState) => state.facts
);

export const selectNetworkMap = createSelector(
  selectNetworkState,
  (state: NetworkState) => state.map
);

export const selectNetworkChanges = createSelector(
  selectNetworkState,
  (state: NetworkState) => state.changes
);
