import { LocationPoiSummaryPage } from '@api/common/poi';
import { LocationPoisPage } from '@api/common/poi';
import { PoiLocationsPage } from '@api/common/poi';
import { ApiResponse } from '@api/custom';

export const initialState: PoiState = {
  locationPoisPageIndex: null,
  locationPoisPage: null,
  locationPoiSummaryPage: null,
  locationPoiSummaryLocations: null,
  poiAreasPage: null,
};

export interface PoiState {
  locationPoisPageIndex: number;
  locationPoisPage: ApiResponse<LocationPoisPage>;
  locationPoiSummaryPage: ApiResponse<LocationPoiSummaryPage>;
  locationPoiSummaryLocations: ApiResponse<PoiLocationsPage>;
  poiAreasPage: ApiResponse<string>;
}

export const poiFeatureKey = 'poi';
