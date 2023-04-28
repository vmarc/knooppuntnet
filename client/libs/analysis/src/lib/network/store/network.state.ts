import { ChangesParameters } from '@api/common/changes/filter';
import { NetworkChangesPage } from '@api/common/network';
import { NetworkDetailsPage } from '@api/common/network';
import { NetworkFactsPage } from '@api/common/network';
import { NetworkMapPage } from '@api/common/network';
import { NetworkNodesPage } from '@api/common/network';
import { NetworkRoutesPage } from '@api/common/network';
import { NetworkSummary } from '@api/common/network';
import { ApiResponse } from '@api/custom';
import { NetworkMapPosition } from '@app/components/ol/domain';

export const initialState: NetworkState = {
  networkId: null,
  summary: null,
  detailsPage: null,
  nodesPage: null,
  routesPage: null,
  factsPage: null,
  mapPage: null,
  mapPositionFromUrl: null,
  changesPage: null,
  changesParameters: null,
};

export interface NetworkState {
  networkId: number;
  summary: NetworkSummary;
  detailsPage: ApiResponse<NetworkDetailsPage>;
  nodesPage: ApiResponse<NetworkNodesPage>;
  routesPage: ApiResponse<NetworkRoutesPage>;
  factsPage: ApiResponse<NetworkFactsPage>;
  mapPage: ApiResponse<NetworkMapPage>;
  mapPositionFromUrl: NetworkMapPosition;
  changesPage: ApiResponse<NetworkChangesPage>;
  changesParameters: ChangesParameters;
}

export const networkFeatureKey = 'network';
