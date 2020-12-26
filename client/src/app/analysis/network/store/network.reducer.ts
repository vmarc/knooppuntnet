import {routerNavigationAction} from '@ngrx/router-store';
import {on} from '@ngrx/store';
import {createReducer} from '@ngrx/store';
import {actionNetworkDetailsPageLoaded} from './network.actions';
import {initialState} from './network.state';

export const networkReducer = createReducer(
  initialState,
  on(
    routerNavigationAction,
    (state, action) => ({
      ...state,
      detailsPage: null,
      nodesPage: null,
      routesPage: null,
      factsPage: null,
      mapPage: null,
      changesPage: null
    })
  ),
  on(
    actionNetworkDetailsPageLoaded,
    (state, {response}) => ({
      ...state,
      detailsPage: response
    })
  ),
  on(
    actionNetworkDetailsPageLoaded,
    (state, {response}) => ({
      ...state,
      nodesPage: response
    })
  ),
  on(
    actionNetworkDetailsPageLoaded,
    (state, {response}) => ({
      ...state,
      routesPage: response
    })
  ),
  on(
    actionNetworkDetailsPageLoaded,
    (state, {response}) => ({
      ...state,
      factsPage: response
    })
  ),
  on(
    actionNetworkDetailsPageLoaded,
    (state, {response}) => ({
      ...state,
      mapPage: response
    })
  ),
  on(
    actionNetworkDetailsPageLoaded,
    (state, {response}) => ({
      ...state,
      changesPage: response
    })
  )
);
