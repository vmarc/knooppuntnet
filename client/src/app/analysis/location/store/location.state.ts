import { LocationChangesPage } from '@api/common/location/location-changes-page';
import { LocationEditPage } from '@api/common/location/location-edit-page';
import { LocationFactsPage } from '@api/common/location/location-facts-page';
import { LocationMapPage } from '@api/common/location/location-map-page';
import { LocationNodesPage } from '@api/common/location/location-nodes-page';
import { LocationRoutesPage } from '@api/common/location/location-routes-page';
import { LocationSummary } from '@api/common/location/location-summary';
import { ApiResponse } from '@api/custom/api-response';
import { LocationKey } from '@api/custom/location-key';
import { LocationNodesType } from '@api/custom/location-nodes-type';
import { LocationRoutesType } from '@api/custom/location-routes-type';
import { AppState } from '../../../core/core.state';

export const initialState: LocationState = {
  locationKey: null,
  locationSummary: null,
  nodesPageType: LocationNodesType.all,
  nodesPageIndex: 0,
  nodesPage: null,
  routesPageType: LocationRoutesType.all,
  routesPageIndex: 0,
  routesPage: null,
  factsPage: null,
  mapPage: null,
  changesPage: null,
  editPage: null,
};

export interface LocationState {
  locationKey: LocationKey;
  locationSummary: LocationSummary;
  nodesPageType: LocationNodesType;
  nodesPageIndex: number;
  nodesPage: ApiResponse<LocationNodesPage>;
  routesPageType: LocationRoutesType;
  routesPageIndex: number;
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
