import { LocationChangesPage } from '@api/common/location';
import { LocationEditPage } from '@api/common/location';
import { LocationFactsPage } from '@api/common/location';
import { LocationMapPage } from '@api/common/location';
import { LocationNodesPage } from '@api/common/location';
import { LocationRoutesPage } from '@api/common/location';
import { LocationSummary } from '@api/common/location';
import { ApiResponse } from '@api/custom';
import { LocationKey } from '@api/custom';
import { LocationNodesType } from '@api/custom';
import { LocationRoutesType } from '@api/custom';

export const initialState: LocationState = {
  locationKey: null,
  locationSummary: null,
  nodesPageType: undefined,
  nodesPageIndex: undefined,
  nodesPage: undefined,
  routesPageType: undefined,
  routesPageIndex: undefined,
  routesPage: undefined,
  factsPage: undefined,
  mapPage: undefined,
  changesPageIndex: undefined,
  changesPage: undefined,
  editPage: undefined,
};

export interface LocationState {
  locationKey: LocationKey;
  locationSummary: LocationSummary;
  nodesPageType: LocationNodesType | undefined;
  nodesPageIndex: number | undefined;
  nodesPage: ApiResponse<LocationNodesPage> | undefined;
  routesPageType: LocationRoutesType | undefined;
  routesPageIndex: number | undefined;
  routesPage: ApiResponse<LocationRoutesPage> | undefined;
  factsPage: ApiResponse<LocationFactsPage> | undefined;
  mapPage: ApiResponse<LocationMapPage> | undefined;
  changesPageIndex: number | undefined;
  changesPage: ApiResponse<LocationChangesPage> | undefined;
  editPage: ApiResponse<LocationEditPage> | undefined;
}

export const locationFeatureKey = 'location';
