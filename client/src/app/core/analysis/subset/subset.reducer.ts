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
    (state, {response}) => {
      return {
        ...state,
        networks: response
      };
    }
  ),
  on(
    actionSubsetFactsLoaded,
    (state, {response}) => {
      return {
        ...state,
        facts: response
      };
    }
  ),
  on(
    actionSubsetOrphanNodesLoaded,
    (state, {response}) => {
      return {
        ...state,
        orphanNodes: response
      };
    }
  ),
  on(
    actionSubsetOrphanRoutesLoaded,
    (state, {response}) => {
      return {
        ...state,
        orphanRoutes: response
      };
    }
  ),
  on(
    actionSubsetMapLoaded,
    (state, {response}) => {
      return {
        ...state,
        map: response
      };
    }
  ),
  on(
    actionSubsetChangesLoaded,
    (state, {response}) => {
      return {
        ...state,
        changes: response
      };
    }
  )
);
