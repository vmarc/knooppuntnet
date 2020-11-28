import {routerNavigationAction} from '@ngrx/router-store';
import {on} from '@ngrx/store';
import {createReducer} from '@ngrx/store';
import {actionRouteChangesLoaded} from './route.actions';
import {actionRouteMapLoaded} from './route.actions';
import {actionRouteDetailsLoaded} from './route.actions';
import {actionRouteLink} from './route.actions';
import {initialState} from './route.state';

export const routeReducer = createReducer(
  initialState,
  on(
    routerNavigationAction,
    (state, action) => ({
      ...state,
      details: null,
      map: null,
      changes: null
    })
  ),
  on(
    actionRouteLink,
    (state, {routeId, routeName}) => ({
      ...state,
      routeId: routeId,
      routeName: routeName,
      changeCount: 0
    })
  ),
  on(
    actionRouteDetailsLoaded,
    (state, {response}) => {
      const routeId = response.result?.route.summary.id.toString() ?? state.routeId;
      const routeName = response.result?.route.summary.name ?? state.routeName;
      const changeCount = response.result?.changeCount ?? state.changeCount;
      return {
        ...state,
        routeId: routeId,
        routeName: routeName,
        changeCount: changeCount,
        details: response
      };
    }
  ),
  on(
    actionRouteMapLoaded,
    (state, {response}) => {
      const routeId = response.result?.route.summary.id.toString() ?? state.routeId;
      const routeName = response.result?.route.summary.name ?? state.routeName;
      const changeCount = response.result?.changeCount ?? state.changeCount;
      return {
        ...state,
        routeId: routeId,
        routeName: routeName,
        changeCount: changeCount,
        map: response
      };
    }
  ),
  on(
    actionRouteChangesLoaded,
    (state, {response}) => {
      const routeId = response.result?.route.summary.id.toString() ?? state.routeId;
      const routeName = response.result?.route.summary.name ?? state.routeName;
      const changeCount = response.result?.changeCount ?? state.changeCount;
      return {
        ...state,
        routeId: routeId,
        routeName: routeName,
        changeCount: changeCount,
        changes: response
      };
    }
  )
);
