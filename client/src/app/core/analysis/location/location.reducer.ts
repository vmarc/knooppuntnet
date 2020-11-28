import {routerNavigationAction} from '@ngrx/router-store';
import {on} from '@ngrx/store';
import {createReducer} from '@ngrx/store';
import {actionLocationNodesLoaded} from './location.actions';
import {actionLocationRoutesLoaded} from './location.actions';
import {actionLocationFactsLoaded} from './location.actions';
import {actionLocationMapLoaded} from './location.actions';
import {actionLocationChangesLoaded} from './location.actions';
import {actionLocationEditLoaded} from './location.actions';
import {initialState} from './location.state';

export const locationReducer = createReducer(
  initialState,
  on(
    routerNavigationAction,
    (state, action) => ({
      ...state,
      nodes: null,
      routes: null,
      facts: null,
      map: null,
      changes: null,
      edit: null
    })
  ),
  on(
    actionLocationNodesLoaded,
    (state, {response}) => {
      return {
        ...state,
        nodes: response
      };
    }
  ),
  on(
    actionLocationRoutesLoaded,
    (state, {response}) => {
      return {
        ...state,
        routes: response
      };
    }
  ),
  on(
    actionLocationFactsLoaded,
    (state, {response}) => {
      return {
        ...state,
        facts: response
      };
    }
  ),
  on(
    actionLocationMapLoaded,
    (state, {response}) => {
      return {
        ...state,
        map: response
      };
    }
  ),
  on(
    actionLocationChangesLoaded,
    (state, {response}) => {
      return {
        ...state,
        changes: response
      };
    }
  ),
  on(
    actionLocationEditLoaded,
    (state, {response}) => {
      return {
        ...state,
        edit: response
      };
    }
  )
);
