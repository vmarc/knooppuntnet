import {createSelector} from '@ngrx/store';
import {selectNodeState} from '../../core.state';
import {NodeState} from './node.state';

export const selectNodeDetails = createSelector(
  selectNodeState,
  (state: NodeState) => state.details
);

export const selectNodeMap = createSelector(
  selectNodeState,
  (state: NodeState) => state.map
);

export const selectNodeChanges = createSelector(
  selectNodeState,
  (state: NodeState) => state.changes
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
