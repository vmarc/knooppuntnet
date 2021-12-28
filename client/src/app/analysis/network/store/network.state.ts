import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { NetworkChangesPage } from '@api/common/network/network-changes-page';
import { NetworkDetailsPage } from '@api/common/network/network-details-page';
import { NetworkFactsPage } from '@api/common/network/network-facts-page';
import { NetworkMapPage } from '@api/common/network/network-map-page';
import { NetworkNodesPage } from '@api/common/network/network-nodes-page';
import { NetworkRoutesPage } from '@api/common/network/network-routes-page';
import { ApiResponse } from '@api/custom/api-response';

export const initialState: NetworkState = {
  detailsPage: null,
  nodesPage: null,
  routesPage: null,
  factsPage: null,
  mapPage: null,
  changesPage: null,
  changesParameters: null,
};

export interface NetworkState {
  detailsPage: ApiResponse<NetworkDetailsPage>;
  nodesPage: ApiResponse<NetworkNodesPage>;
  routesPage: ApiResponse<NetworkRoutesPage>;
  factsPage: ApiResponse<NetworkFactsPage>;
  mapPage: ApiResponse<NetworkMapPage>;
  changesPage: ApiResponse<NetworkChangesPage>;
  changesParameters: ChangesParameters;
}

export const networkFeatureKey = 'network';
