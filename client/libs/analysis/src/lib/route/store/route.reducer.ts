import { on } from '@ngrx/store';
import { createReducer } from '@ngrx/store';
import { actionRouteChangesPageDestroy } from './route.actions';
import { actionRouteMapPageDestroy } from './route.actions';
import { actionRouteDetailsPageDestroy } from './route.actions';
import { actionRouteMapPageLoad } from './route.actions';
import { actionRouteDetailsPageLoad } from './route.actions';
import { actionRouteChangesPageLoad } from './route.actions';
import { actionRouteChangesPageImpact } from './route.actions';
import { actionRouteChangesPageSize } from './route.actions';
import { actionRouteChangesFilterOption } from './route.actions';
import { actionRouteChangesPageIndex } from './route.actions';
import { actionRouteChangesPageLoaded } from './route.actions';
import { actionRouteMapPageLoaded } from './route.actions';
import { actionRouteDetailsPageLoaded } from './route.actions';
import { RouteState } from './route.state';
import { initialState } from './route.state';

export const routeReducer = createReducer<RouteState>(
  initialState,
  on(
    actionRouteDetailsPageLoad,
    (state, { routeId, routeName, networkType }): RouteState => {
      return {
        ...state,
        routeId,
        routeName,
        networkType,
      };
    }
  ),
  on(
    actionRouteMapPageLoad,
    (state, { routeId }): RouteState => ({
      ...state,
      routeId,
    })
  ),
  on(actionRouteDetailsPageLoaded, (state, response): RouteState => {
    const routeId =
      response.result?.route.summary.id.toString() ?? state.routeId;
    const routeName = response.result?.route.summary.name ?? state.routeName;
    const networkType =
      response.result?.route.summary.networkType ?? state.networkType;
    const changeCount = response.result?.changeCount ?? state.changeCount;
    return {
      ...state,
      routeId,
      routeName,
      networkType,
      changeCount,
      detailsPage: response,
    };
  }),
  on(actionRouteDetailsPageDestroy, (state): RouteState => {
    return {
      ...state,
      detailsPage: null,
    };
  }),
  on(
    actionRouteMapPageLoaded,
    (state, { response, mapPositionFromUrl }): RouteState => {
      const routeId =
        response.result?.routeMapInfo.routeId.toString() ?? state.routeId;
      const routeName =
        response.result?.routeMapInfo.routeName ?? state.routeName;
      const networkType =
        response.result?.routeMapInfo.networkType ?? state.networkType;
      const changeCount = response.result?.changeCount ?? state.changeCount;
      return {
        ...state,
        routeId,
        routeName,
        networkType,
        changeCount,
        mapPage: response,
        mapPositionFromUrl,
      };
    }
  ),
  on(actionRouteMapPageDestroy, (state): RouteState => {
    return {
      ...state,
      mapPage: null,
      mapPositionFromUrl: null,
    };
  }),
  on(
    actionRouteChangesPageLoad,
    (state, { routeId, changesParameters }): RouteState => ({
      ...state,
      routeId,
      changesParameters,
    })
  ),
  on(actionRouteChangesPageLoaded, (state, response): RouteState => {
    const routeId =
      response.result?.routeNameInfo.routeId.toString() ?? state.routeId;
    const routeName =
      response.result?.routeNameInfo.routeName ?? state.routeName;
    const changeCount = response.result?.changeCount ?? 0;
    const networkType =
      response.result?.routeNameInfo.networkType ?? state.networkType;
    return {
      ...state,
      routeId,
      routeName,
      networkType,
      changeCount,
      changesPage: response,
    };
  }),
  on(actionRouteChangesPageDestroy, (state): RouteState => {
    return {
      ...state,
      changesPage: null,
    };
  }),
  on(
    actionRouteChangesPageImpact,
    (state, action): RouteState => ({
      ...state,
      changesParameters: {
        ...state.changesParameters,
        impact: action.impact,
        pageIndex: 0,
      },
    })
  ),
  on(
    actionRouteChangesPageSize,
    (state, action): RouteState => ({
      ...state,
      changesParameters: {
        ...state.changesParameters,
        pageSize: action.pageSize,
        pageIndex: 0,
      },
    })
  ),
  on(actionRouteChangesPageIndex, (state, action): RouteState => {
    return {
      ...state,
      changesParameters: {
        ...state.changesParameters,
        pageIndex: action.pageIndex,
      },
    };
  }),
  on(
    actionRouteChangesFilterOption,
    (state, action): RouteState => ({
      ...state,
      changesParameters: {
        ...state.changesParameters,
        year: action.option.year,
        month: action.option.month,
        day: action.option.day,
        impact: action.option.impact,
        pageIndex: 0,
      },
    })
  )
);
