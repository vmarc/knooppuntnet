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
import { MapLayerState } from '@app/components/ol/domain/map-layer-state';
import { MapPosition } from '@app/components/ol/domain/map-position';

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
  mapPosition: undefined,
  mapLayerStates: [],
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
  mapPosition: MapPosition | undefined;
  mapLayerStates: MapLayerState[];
  changesPageIndex: number | undefined;
  changesPage: ApiResponse<LocationChangesPage> | undefined;
  editPage: ApiResponse<LocationEditPage> | undefined;
}

export const locationFeatureKey = 'location';
