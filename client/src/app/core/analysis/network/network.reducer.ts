import {routerNavigationAction} from '@ngrx/router-store';
import {on} from '@ngrx/store';
import {createReducer} from '@ngrx/store';
import {actionNetworkDetailsLoaded} from './network.actions';
import {initialState} from './network.state';

export const networkReducer = createReducer(
  initialState,
  on(
    routerNavigationAction,
    (state, action) => ({
      ...state,
      details: null,
      nodes: null,
      routes: null,
      facts: null,
      map: null,
      changes: null
    })
  ),
  on(
    actionNetworkDetailsLoaded,
    (state, {response}) => {
      return {
        ...state,
        details: response
      };
    }
  ),
  on(
    actionNetworkDetailsLoaded,
    (state, {response}) => {
      return {
        ...state,
        nodes: response
      };
    }
  ),
  on(
    actionNetworkDetailsLoaded,
    (state, {response}) => {
      return {
        ...state,
        routes: response
      };
    }
  ),
  on(
    actionNetworkDetailsLoaded,
    (state, {response}) => {
      return {
        ...state,
        facts: response
      };
    }
  ),
  on(
    actionNetworkDetailsLoaded,
    (state, {response}) => {
      return {
        ...state,
        map: response
      };
    }
  ),
  on(
    actionNetworkDetailsLoaded,
    (state, {response}) => {
      return {
        ...state,
        changes: response
      };
    }
  )
);
