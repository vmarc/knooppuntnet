import {routerNavigationAction} from '@ngrx/router-store';
import {on} from '@ngrx/store';
import {createReducer} from '@ngrx/store';
import {actionSubsetNetworksLoaded} from './subset.actions';
import {actionSubsetFactsLoaded} from './subset.actions';
import {actionSubsetOrphanNodesLoaded} from './subset.actions';
import {actionSubsetOrphanRoutesLoaded} from './subset.actions';
import {actionSubsetMapLoaded} from './subset.actions';
import {actionSubsetChangesLoaded} from './subset.actions';
import {initialState} from './subset.state';

export const subsetReducer = createReducer(
  initialState,
  on(
    routerNavigationAction,
    (state, action) => ({
      ...state,
      networks: null,
      facts: null,
      orphanNodes: null,
      orphanRoutes: null,
      map: null,
      changes: null
    })
  ),
  on(
    actionSubsetNetworksLoaded,
    (state, {response}) => ({
      ...state,
      networks: response
    })
  ),
  on(
    actionSubsetFactsLoaded,
    (state, {response}) => ({
      ...state,
      facts: response
    })
  ),
  on(
    actionSubsetOrphanNodesLoaded,
    (state, {response}) => ({
      ...state,
      orphanNodes: response
    })
  ),
  on(
    actionSubsetOrphanRoutesLoaded,
    (state, {response}) => ({
      ...state,
      orphanRoutes: response
    })
  ),
  on(
    actionSubsetMapLoaded,
    (state, {response}) => ({
      ...state,
      map: response
    })
  ),
  on(
    actionSubsetChangesLoaded,
    (state, {response}) => ({
      ...state,
      changes: response
    })
  )
);
