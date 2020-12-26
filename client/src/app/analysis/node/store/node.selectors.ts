import {createFeatureSelector} from '@ngrx/store';
import {createSelector} from '@ngrx/store';
import {NodeRootState} from './node.state';
import {nodeFeatureKey} from './node.state';
import {NodeState} from './node.state';

export const selectNodeState = createFeatureSelector<NodeRootState, NodeState>(nodeFeatureKey);

export const selectNodeDetailsPage = createSelector(
  selectNodeState,
  (state: NodeState) => state.detailsPage
);

export const selectNodeMapPage = createSelector(
  selectNodeState,
  (state: NodeState) => state.mapPage
);

export const selectNodeChangesPage = createSelector(
  selectNodeState,
  (state: NodeState) => state.changesPage
);

export const selectNodeId = createSelector(
  selectNodeState,
  (state: NodeState) => state.nodeId
);

export const selectNodeName = createSelector(
  selectNodeState,
  (state: NodeState) => state.nodeName
);

export const selectNodeChangeCount = createSelector(
  selectNodeState,
  (state: NodeState) => state.changeCount
);
