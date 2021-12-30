import { routerNavigationAction } from '@ngrx/router-store';
import { on } from '@ngrx/store';
import { createReducer } from '@ngrx/store';
import { actionPreferencesItemsPerPage } from '../../../core/preferences/preferences.actions';
import { actionPreferencesImpact } from '../../../core/preferences/preferences.actions';
import { actionRouteChangesFilterOption } from './route.actions';
import { actionRouteChangesPageIndex } from './route.actions';
import { actionRouteId } from './route.actions';
import { actionRouteChangesPageLoaded } from './route.actions';
import { actionRouteMapPageLoaded } from './route.actions';
import { actionRouteDetailsPageLoaded } from './route.actions';
import { actionRouteLink } from './route.actions';
import { initialState } from './route.state';

export const routeReducer = createReducer(
  initialState,
  on(routerNavigationAction, (state, {}) => ({
    ...state,
    detailsPage: null,
    mapPage: null,
    changesPage: null,
  })),
  on(actionRouteId, (state, { routeId }) => ({
    ...state,
    routeId,
  })),
  on(actionRouteLink, (state, { routeId, routeName, networkType }) => ({
    ...state,
    routeId,
    routeName,
    networkType,
    changeCount: 0,
  })),
  on(actionRouteDetailsPageLoaded, (state, { response }) => {
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
  on(actionRouteMapPageLoaded, (state, { response }) => {
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
    };
  }),
  on(actionRouteChangesPageLoaded, (state, { response }) => {
    const routeId =
      response.result?.routeNameInfo.routeId.toString() ?? state.routeId;
    const routeName =
      response.result?.routeNameInfo.routeName ?? state.routeName;
    const changeCount = response.result?.changeCount ?? state.changeCount;
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
  on(actionPreferencesImpact, (state, action) => ({
    ...state,
    changesParameters: {
      ...state.changesParameters,
      impact: action.impact,
      pageIndex: 0,
    },
  })),
  on(actionPreferencesItemsPerPage, (state, action) => ({
    ...state,
    changesParameters: {
      ...state.changesParameters,
      itemsPerPage: action.itemsPerPage,
      pageIndex: 0,
    },
  })),
  on(actionRouteChangesPageIndex, (state, action) => {
    return {
      ...state,
      changesParameters: {
        ...state.changesParameters,
        pageIndex: action.pageIndex,
      },
    };
  }),
  on(actionRouteChangesFilterOption, (state, action) => ({
    ...state,
    changesParameters: {
      ...state.changesParameters,
      year: action.option.year,
      month: action.option.month,
      day: action.option.day,
      impact: action.option.impact,
      pageIndex: 0,
    },
  }))
);
