import {ApiResponse} from '../../../kpn/api/custom/api-response';
import {LocationNodesPage} from '../../../kpn/api/common/location/location-nodes-page';
import {LocationRoutesPage} from '../../../kpn/api/common/location/location-routes-page';
import {LocationFactsPage} from '../../../kpn/api/common/location/location-facts-page';
import {LocationMapPage} from '../../../kpn/api/common/location/location-map-page';
import {LocationChangesPage} from '../../../kpn/api/common/location/location-changes-page';
import {LocationEditPage} from '../../../kpn/api/common/location/location-edit-page';

export const initialState: LocationState = {
  // selection: null,
  nodes: null,
  routes: null,
  facts: null,
  map: null,
  changes: null,
  edit: null
};

export interface LocationState {
  // selection: ???;
  nodes: ApiResponse<LocationNodesPage>;
  routes: ApiResponse<LocationRoutesPage>;
  facts: ApiResponse<LocationFactsPage>;
  map: ApiResponse<LocationMapPage>;
  changes: ApiResponse<LocationChangesPage>;
  edit: ApiResponse<LocationEditPage>;
}
