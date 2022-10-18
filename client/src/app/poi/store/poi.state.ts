import { LocationPoisPage } from '@api/common/poi/location-pois-page';
import { ApiResponse } from '@api/custom/api-response';

export const initialState: PoiState = {
  locationPoisPageIndex: null,
  locationPoisPage: null,
};

export interface PoiState {
  locationPoisPageIndex: number;
  locationPoisPage: ApiResponse<LocationPoisPage>;
}

export const poiFeatureKey = 'poi';
