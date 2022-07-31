import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { NodeDetailsPage } from '@api/common/node/node-details-page';
import { ApiResponse } from '@api/custom/api-response';
import { createFeatureSelector } from '@ngrx/store';
import { createSelector } from '@ngrx/store';
import { NetworkTypes } from '../../../kpn/common/network-types';
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

export const selectNodeMapPositionFromUrl = createSelector(
  selectNodeState,
  (state: NodeState) => state.mapPositionFromUrl
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

export const selectNodeNetworkTypes = createSelector(
  selectNodeDetailsPage,
  (response: ApiResponse<NodeDetailsPage>) => {
    if (response?.result) {
      const networkTypes = response.result.nodeInfo.names.map(
        (nodeName) => nodeName.networkType
      );
      return NetworkTypes.all.filter((networkType) =>
        networkTypes.includes(networkType)
      );
    } else {
      return [];
    }
  }
);
