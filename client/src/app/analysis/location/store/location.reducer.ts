import {routerNavigationAction} from '@ngrx/router-store';
import {on} from '@ngrx/store';
import {createReducer} from '@ngrx/store';
import {actionLocationNodesPageLoaded} from './location.actions';
import {actionLocationRoutesPageLoaded} from './location.actions';
import {actionLocationFactsPageLoaded} from './location.actions';
import {actionLocationMapPageLoaded} from './location.actions';
import {actionLocationChangesPageLoaded} from './location.actions';
import {actionLocationEditPageLoaded} from './location.actions';
import {initialState} from './location.state';

export const locationReducer = createReducer(
  initialState,
  on(
    routerNavigationAction,
    (state, action) => ({
      ...state,
      nodesPage: null,
      routesPage: null,
      factsPage: null,
      mapPage: null,
      changesPage: null,
      editPage: null
    })
  ),
  on(
    actionLocationNodesPageLoaded,
    (state, {response}) => ({
      ...state,
      nodesPage: response
    })
  ),
  on(
    actionLocationRoutesPageLoaded,
    (state, {response}) => ({
      ...state,
      routesPage: response
    })
  ),
  on(
    actionLocationFactsPageLoaded,
    (state, {response}) => ({
      ...state,
      factsPage: response
    })
  ),
  on(
    actionLocationMapPageLoaded,
    (state, {response}) => ({
      ...state,
      mapPage: response
    })
  ),
  on(
    actionLocationChangesPageLoaded,
    (state, {response}) => ({
      ...state,
      changesPage: response
    })
  ),
  on(
    actionLocationEditPageLoaded,
    (state, {response}) => ({
      ...state,
      editPage: response
    })
  )
);
