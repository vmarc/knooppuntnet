import { ChangesParameters } from '@api/common/changes/filter';
import { NodeChangesPage } from '@api/common/node';
import { NodeDetailsPage } from '@api/common/node';
import { NodeMapPage } from '@api/common/node';
import { ApiResponse } from '@api/custom';
import { MapPosition } from '@app/components/ol/domain';

export const initialState: NodeState = {
  nodeId: '',
  nodeName: '',
  changeCount: 0,
  detailsPage: null,
  mapPage: null,
  mapPositionFromUrl: null,
  changesPage: null,
  changesParameters: null,
};

export interface NodeState {
  nodeId: string;
  nodeName: string;
  changeCount: number;
  detailsPage: ApiResponse<NodeDetailsPage>;
  mapPage: ApiResponse<NodeMapPage>;
  mapPositionFromUrl: MapPosition;
  changesPage: ApiResponse<NodeChangesPage>;
  changesParameters: ChangesParameters;
}

export const nodeFeatureKey = 'node';
