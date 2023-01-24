import { routerNavigationAction } from '@ngrx/router-store';
import { createReducer } from '@ngrx/store';
import { on } from '@ngrx/store';
import { actionLocationPoiSummaryLocationsLoaded } from './poi.actions';
import { actionLocationPoiSummaryPageLoaded } from './poi.actions';
import { actionLocationPoisPageInit } from './poi.actions';
import { actionLocationPoisPageIndex } from './poi.actions';
import { actionLocationPoisPageLoaded } from './poi.actions';
import { initialState } from './poi.state';

export const poiReducer = createReducer<PoiState>(
  initialState,
  on(
    routerNavigationAction,
    (state): PoiState => ({
      ...state,
      locationPoisPage: null,
      locationPoiSummaryPage: null,
    })
  ),
  on(
    actionLocationPoisPageInit,
    (state, response): PoiState => ({
      ...state,
      locationPoisPageIndex: 0,
    })
  ),
  on(
    actionLocationPoisPageLoaded,
    (state, response): PoiState => ({
      ...state,
      locationPoisPage: response,
    })
  ),
  on(
    actionLocationPoisPageIndex,
    (state, { pageIndex }): PoiState => ({
      ...state,
      locationPoisPageIndex: pageIndex,
    })
  ),
  on(
    actionLocationPoiSummaryPageLoaded,
    (state, response): PoiState => ({
      ...state,
      locationPoiSummaryPage: response,
    })
  ),
  on(
    actionLocationPoiSummaryLocationsLoaded,
    (state, response): PoiState => ({
      ...state,
      locationPoiSummaryLocations: response,
    })
  )
);
