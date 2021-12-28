import { NetworkChangesPage } from '@api/common/network/network-changes-page';
import { NetworkSummary } from '@api/common/network/network-summary';
import { ApiResponse } from '@api/custom/api-response';
import { routerNavigationAction } from '@ngrx/router-store';
import { on } from '@ngrx/store';
import { createReducer } from '@ngrx/store';
import { RoutingUtil } from '../../../base/routing-util';
import { actionPreferencesItemsPerPage } from '../../../core/preferences/preferences.actions';
import { actionPreferencesImpact } from '../../../core/preferences/preferences.actions';
import { actionNetworkLink } from './network.actions';
import { actionNetworkId } from './network.actions';
import { actionNetworkChangesFilterOption } from './network.actions';
import { actionNetworkChangesPageIndex } from './network.actions';
import { actionNetworkChangesPageInit } from './network.actions';
import { actionNetworkDetailsPageLoaded } from './network.actions';
import { actionNetworkNodesPageLoaded } from './network.actions';
import { actionNetworkRoutesPageLoaded } from './network.actions';
import { actionNetworkFactsPageLoaded } from './network.actions';
import { actionNetworkMapPageLoaded } from './network.actions';
import { actionNetworkChangesPageLoaded } from './network.actions';
import { initialState } from './network.state';

export const networkReducer = createReducer(
  initialState,
  on(routerNavigationAction, (state, action) => {
    const util = new RoutingUtil(action);
    let changesPage: ApiResponse<NetworkChangesPage> = null;
    if (util.isNetworkChangesPage()) {
      changesPage = state.changesPage;
    }
    return {
      ...state,
      detailsPage: null,
      nodesPage: null,
      routesPage: null,
      factsPage: null,
      mapPage: null,
      changesPage,
    };
  }),
  on(actionNetworkId, (state, { networkId }) => {
    if (networkId === state.networkId) {
      return state;
    }

    const summary: NetworkSummary = {
      name: null,
      networkType: null,
      networkScope: null,
      factCount: 0,
      nodeCount: 0,
      routeCount: 0,
      changeCount: 0,
    };

    return {
      ...state,
      networkId,
      summary,
    };
  }),
  on(actionNetworkLink, (state, { networkId, networkName, networkType }) => {
    const summary: NetworkSummary = {
      name: networkName,
      networkType,
      networkScope: null,
      factCount: 0,
      nodeCount: 0,
      routeCount: 0,
      changeCount: 0,
    };

    return {
      ...state,
      networkId,
      summary,
    };
  }),
  on(actionNetworkDetailsPageLoaded, (state, { response }) => ({
    ...state,
    summary: response.result.summary,
    detailsPage: response,
  })),
  on(actionNetworkNodesPageLoaded, (state, { response }) => ({
    ...state,
    summary: response.result.summary,
    nodesPage: response,
  })),
  on(actionNetworkRoutesPageLoaded, (state, { response }) => ({
    ...state,
    summary: response.result.summary,
    routesPage: response,
  })),
  on(actionNetworkFactsPageLoaded, (state, { response }) => ({
    ...state,
    summary: response.result.summary,
    factsPage: response,
  })),
  on(actionNetworkMapPageLoaded, (state, { response }) => ({
    ...state,
    summary: response.result.summary,
    mapPage: response,
  })),
  on(actionNetworkChangesPageInit, (state, {}) => ({
    ...state,
    changesParameters: {
      ...state.changesParameters,
      pageIndex: 0,
    },
  })),
  on(actionNetworkChangesPageLoaded, (state, { response }) => ({
    ...state,
    summary: response.result.network,
    changesPage: response,
  })),
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
  on(actionNetworkChangesPageIndex, (state, action) => ({
    ...state,
    changesParameters: {
      ...state.changesParameters,
      pageIndex: action.pageIndex,
    },
  })),
  on(actionNetworkChangesFilterOption, (state, action) => ({
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
