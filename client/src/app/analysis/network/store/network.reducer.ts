import { routerNavigationAction } from '@ngrx/router-store';
import { on } from '@ngrx/store';
import { createReducer } from '@ngrx/store';
import { actionNetworkDetailsPageLoaded } from './network.actions';
import { actionNetworkNodesPageLoaded } from './network.actions';
import { actionNetworkRoutesPageLoaded } from './network.actions';
import { actionNetworkFactsPageLoaded } from './network.actions';
import { actionNetworkMapPageLoaded } from './network.actions';
import { actionNetworkChangesPageLoaded } from './network.actions';
import { initialState } from './network.state';

export const networkReducer = createReducer(
  initialState,
  on(routerNavigationAction, (state, action) => ({
    ...state,
    detailsPage: null,
    nodesPage: null,
    routesPage: null,
    factsPage: null,
    mapPage: null,
    changesPage: null,
  })),
  on(actionNetworkDetailsPageLoaded, (state, { response }) => ({
    ...state,
    detailsPage: response,
  })),
  on(actionNetworkNodesPageLoaded, (state, { response }) => ({
    ...state,
    nodesPage: response,
  })),
  on(actionNetworkRoutesPageLoaded, (state, { response }) => ({
    ...state,
    routesPage: response,
  })),
  on(actionNetworkFactsPageLoaded, (state, { response }) => ({
    ...state,
    factsPage: response,
  })),
  on(actionNetworkMapPageLoaded, (state, { response }) => ({
    ...state,
    mapPage: response,
  })),
  on(actionNetworkChangesPageLoaded, (state, { response }) => ({
    ...state,
    changesPage: response,
  }))
);
