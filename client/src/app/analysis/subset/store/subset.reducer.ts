import { routerNavigationAction } from '@ngrx/router-store';
import { on } from '@ngrx/store';
import { createReducer } from '@ngrx/store';
import { actionSubsetNetworksPageLoaded } from './subset.actions';
import { actionSubsetFactsPageLoaded } from './subset.actions';
import { actionSubsetOrphanNodesPageLoaded } from './subset.actions';
import { actionSubsetOrphanRoutesPageLoaded } from './subset.actions';
import { actionSubsetMapPageLoaded } from './subset.actions';
import { actionSubsetChangesPageLoaded } from './subset.actions';
import { initialState } from './subset.state';

export const subsetReducer = createReducer(
  initialState,
  on(routerNavigationAction, (state, action) => ({
    ...state,
    networksPage: null,
    factsPage: null,
    orphanNodesPage: null,
    orphanRoutesPage: null,
    mapPage: null,
    changesPage: null,
  })),
  on(actionSubsetNetworksPageLoaded, (state, { response }) => ({
    ...state,
    networksPage: response,
  })),
  on(actionSubsetFactsPageLoaded, (state, { response }) => ({
    ...state,
    factsPage: response,
  })),
  on(actionSubsetOrphanNodesPageLoaded, (state, { response }) => ({
    ...state,
    orphanNodesPage: response,
  })),
  on(actionSubsetOrphanRoutesPageLoaded, (state, { response }) => ({
    ...state,
    orphanRoutesPage: response,
  })),
  on(actionSubsetMapPageLoaded, (state, { response }) => ({
    ...state,
    mapPage: response,
  })),
  on(actionSubsetChangesPageLoaded, (state, { response }) => ({
    ...state,
    changesPage: response,
  }))
);
