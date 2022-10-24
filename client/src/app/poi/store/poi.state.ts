import { LocationPoiSummaryPage } from '@api/common/poi/location-poi-summary-page';
import { LocationPoisPage } from '@api/common/poi/location-pois-page';
import { PoiLocationsPage } from '@api/common/poi/poi-locations-page';
import { ApiResponse } from '@api/custom/api-response';

export const initialState: PoiState = {
  locationPoisPageIndex: null,
  locationPoisPage: null,
  locationPoiSummaryPage: null,
  locationPoiSummaryLocations: null,
};

export interface PoiState {
  locationPoisPageIndex: number;
  locationPoisPage: ApiResponse<LocationPoisPage>;
  locationPoiSummaryPage: ApiResponse<LocationPoiSummaryPage>;
  locationPoiSummaryLocations: ApiResponse<PoiLocationsPage>;
}

export const poiFeatureKey = 'poi';
