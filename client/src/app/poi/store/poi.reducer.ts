import { routerNavigationAction } from '@ngrx/router-store';
import { createReducer } from '@ngrx/store';
import { on } from '@ngrx/store';
import { actionLocationPoisPageInit } from './poi.actions';
import { actionLocationPoisPageIndex } from './poi.actions';
import { actionLocationPoisPageLoaded } from './poi.actions';
import { initialState } from './poi.state';

export const poiReducer = createReducer(
  initialState,
  on(routerNavigationAction, (state) => ({
    ...state,
    locationPoisPage: null,
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
  }))
);
