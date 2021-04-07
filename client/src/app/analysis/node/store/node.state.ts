import { NodeChangesPage } from '@api/common/node/node-changes-page';
import { NodeDetailsPage } from '@api/common/node/node-details-page';
import { NodeMapPage } from '@api/common/node/node-map-page';
import { ApiResponse } from '@api/custom/api-response';
import { AppState } from '../../../core/core.state';

export const initialState: NodeState = {
  nodeId: '',
  nodeName: '',
  changeCount: 0,
  detailsPage: null,
  mapPage: null,
  changesPage: null,
};

export interface NodeState {
  nodeId: string;
  nodeName: string;
  changeCount: number;
  detailsPage: ApiResponse<NodeDetailsPage>;
  mapPage: ApiResponse<NodeMapPage>;
  changesPage: ApiResponse<NodeChangesPage>;
}

export const nodeFeatureKey = 'node';

export interface NodeRootState extends AppState {
  [nodeFeatureKey]: NodeState;
}
