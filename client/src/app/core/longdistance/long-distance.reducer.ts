import {routerNavigationAction} from '@ngrx/router-store';
import {createReducer} from '@ngrx/store';
import {on} from '@ngrx/store';
import {actionLongDistanceRouteMapFocus} from './long-distance.actions';
import {actionLongDistanceRoutesLoaded} from './long-distance.actions';
import {actionLongDistanceRouteChangesLoaded} from './long-distance.actions';
import {actionLongDistanceRouteMapLoaded} from './long-distance.actions';
import {actionLongDistanceRouteDetailsLoaded} from './long-distance.actions';
import {initialState} from './long-distance.state';

export const longDistanceReducer = createReducer(
  initialState,
  on(
    routerNavigationAction,
    (state, action) => ({
      ...state,
      routes: null,
      details: null,
      map: null,
      changes: null
    })
  ),
  on(
    actionLongDistanceRoutesLoaded,
    (state, {response}) => {
      return {
        ...state,
        routes: response
      };
    }
  ),
  on(
    actionLongDistanceRouteDetailsLoaded,
    (state, {response}) => {
      const routeId = response.result?.id ?? state.routeId;
      const routeName = response.result?.name ?? state.routeName;
      return {
        ...state,
        routeId,
        routeName,
        details: response
      };
    }
  ),
  on(
    actionLongDistanceRouteMapLoaded,
    (state, {response}) => {
      const routeId = response.result?.id ?? state.routeId;
      const routeName = response.result?.name ?? state.routeName;
      return {
        ...state,
        routeId,
        routeName,
        map: response
      };
    }
  ),
  on(
    actionLongDistanceRouteChangesLoaded,
    (state, {response}) => {
      const routeId = response.result?.id ?? state.routeId;
      const routeName = response.result?.name ?? state.routeName;
      return {
        ...state,
        routeId,
        routeName,
        changes: response
      };
    }
  ),
  on(
    actionLongDistanceRouteMapFocus,
    (state, {segmentId, bounds}) => {
      return {
        ...state,
        mapFocusNokSegmentId: segmentId,
        mapFocus: bounds
      };
    }
  )
);
