import {LocationChangesPage} from '@api/common/location/location-changes-page';
import {LocationEditPage} from '@api/common/location/location-edit-page';
import {LocationFactsPage} from '@api/common/location/location-facts-page';
import {LocationMapPage} from '@api/common/location/location-map-page';
import {LocationNodesPage} from '@api/common/location/location-nodes-page';
import {LocationRoutesPage} from '@api/common/location/location-routes-page';
import {ApiResponse} from '@api/custom/api-response';

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