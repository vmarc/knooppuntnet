import { createFeatureSelector } from '@ngrx/store';
import { createSelector } from '@ngrx/store';
import { PoiState } from './poi.state';
import { poiFeatureKey } from './poi.state';

export const selectPoiState = createFeatureSelector<PoiState>(poiFeatureKey);

export const selectLocationPoisPage = createSelector(
  selectPoiState,
  (state: PoiState) => state.locationPoisPage
);

export const selectLocationPoisPageIndex = createSelector(
  selectPoiState,
  (state: PoiState) => state.locationPoisPageIndex
);

export const selectLocationPoiSummaryPage = createSelector(
  selectPoiState,
  (state: PoiState) => state.locationPoiSummaryPage
);
