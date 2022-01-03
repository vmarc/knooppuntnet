import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { createFeatureSelector } from '@ngrx/store';
import { createSelector } from '@ngrx/store';
import { nodeFeatureKey } from './node.state';
import { NodeState } from './node.state';

export const selectNodeState = createFeatureSelector<NodeState>(nodeFeatureKey);

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

export const selectNodeChangesParameters = createSelector(
  selectNodeState,
  (state: NodeState) => state.changesParameters
);

export const selectNodeChangesFilterOptions = createSelector(
  selectNodeState,
  (state: NodeState) => state.changesPage?.result?.filterOptions
);

export const selectNodeChangesPageImpact = createSelector(
  selectNodeChangesParameters,
  (changesParameters: ChangesParameters) => changesParameters.impact
);

export const selectNodeChangesPageSize = createSelector(
  selectNodeChangesParameters,
  (changesParameters: ChangesParameters) => changesParameters.pageSize
);

export const selectNodeChangesPageIndex = createSelector(
  selectNodeChangesParameters,
  (changesParameters: ChangesParameters) => changesParameters.pageIndex
);
