import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { NetworkChangesPage } from '@api/common/network/network-changes-page';
import { NetworkDetailsPage } from '@api/common/network/network-details-page';
import { NetworkFactsPage } from '@api/common/network/network-facts-page';
import { NetworkMapPage } from '@api/common/network/network-map-page';
import { NetworkNodesPage } from '@api/common/network/network-nodes-page';
import { NetworkRoutesPage } from '@api/common/network/network-routes-page';
import { NetworkSummary } from '@api/common/network/network-summary';
import { ApiResponse } from '@api/custom/api-response';
import { NetworkMapPosition } from '../../../components/ol/domain/network-map-position';

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
