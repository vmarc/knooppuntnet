import {LocationChangesPage} from '@api/common/location/location-changes-page';
import {LocationEditPage} from '@api/common/location/location-edit-page';
import {LocationFactsPage} from '@api/common/location/location-facts-page';
import {LocationMapPage} from '@api/common/location/location-map-page';
import {LocationNodesPage} from '@api/common/location/location-nodes-page';
import {LocationRoutesPage} from '@api/common/location/location-routes-page';
import {ApiResponse} from '@api/custom/api-response';
import {AppState} from '../../../core/core.state';

export const initialState: LocationState = {
  nodesPage: null,
  routesPage: null,
  factsPage: null,
  mapPage: null,
  changesPage: null,
  editPage: null
};

export interface LocationState {
  nodesPage: ApiResponse<LocationNodesPage>;
  routesPage: ApiResponse<LocationRoutesPage>;
  factsPage: ApiResponse<LocationFactsPage>;
  mapPage: ApiResponse<LocationMapPage>;
  changesPage: ApiResponse<LocationChangesPage>;
  editPage: ApiResponse<LocationEditPage>;
}

export const locationFeatureKey = 'location';

export interface LocationRootState extends AppState {
  [locationFeatureKey]: LocationState;
}
