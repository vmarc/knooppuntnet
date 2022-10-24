import { routerNavigationAction } from '@ngrx/router-store';
import { createReducer } from '@ngrx/store';
import { on } from '@ngrx/store';
import { actionLocationPoiSummaryLocationsLoaded } from './poi.actions';
import { actionLocationPoiSummaryPageLoaded } from './poi.actions';
import { actionLocationPoisPageInit } from './poi.actions';
import { actionLocationPoisPageIndex } from './poi.actions';
import { actionLocationPoisPageLoaded } from './poi.actions';
import { initialState } from './poi.state';

export const poiReducer = createReducer(
  initialState,
  on(routerNavigationAction, (state) => ({
    ...state,
    locationPoisPage: null,
    locationPoiSummaryPage: null,
  })),
  on(actionLocationPoisPageInit, (state, response) => ({
    ...state,
    locationPoisPageIndex: 0,
  })),
  on(actionLocationPoisPageLoaded, (state, response) => ({
    ...state,
    locationPoisPage: response,
  })),
  on(actionLocationPoisPageIndex, (state, { pageIndex }) => ({
    ...state,
    locationPoisPageIndex: pageIndex,
  })),
  on(actionLocationPoiSummaryPageLoaded, (state, response) => ({
    ...state,
    locationPoiSummaryPage: response,
  })),
  on(actionLocationPoiSummaryLocationsLoaded, (state, response) => ({
    ...state,
    locationPoiSummaryLocations: response,
  }))
);
