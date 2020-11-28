import {ApiResponse} from '../../../kpn/api/custom/api-response';
import {NetworkDetailsPage} from '../../../kpn/api/common/network/network-details-page';
import {NetworkNodesPage} from '../../../kpn/api/common/network/network-nodes-page';
import {NetworkRoutesPage} from '../../../kpn/api/common/network/network-routes-page';
import {NetworkFactsPage} from '../../../kpn/api/common/network/network-facts-page';
import {NetworkMapPage} from '../../../kpn/api/common/network/network-map-page';
import {NetworkChangesPage} from '../../../kpn/api/common/network/network-changes-page';

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
