import {NetworkChangesPage} from '@api/common/network/network-changes-page';
import {NetworkDetailsPage} from '@api/common/network/network-details-page';
import {NetworkFactsPage} from '@api/common/network/network-facts-page';
import {NetworkMapPage} from '@api/common/network/network-map-page';
import {NetworkNodesPage} from '@api/common/network/network-nodes-page';
import {NetworkRoutesPage} from '@api/common/network/network-routes-page';
import {ApiResponse} from '@api/custom/api-response';

export const initialState: NetworkState = {
  details: null,
  nodes: null,
  routes: null,
  facts: null,
  map: null,
  changes: null
};

export interface NetworkState {
  details: ApiResponse<NetworkDetailsPage>;
  nodes: ApiResponse<NetworkNodesPage>;
  routes: ApiResponse<NetworkRoutesPage>;
  facts: ApiResponse<NetworkFactsPage>;
  map: ApiResponse<NetworkMapPage>;
  changes: ApiResponse<NetworkChangesPage>;
}
