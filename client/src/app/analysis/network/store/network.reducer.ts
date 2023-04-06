import { NetworkChangesPage } from '@api/common/network/network-changes-page';
import { NetworkSummary } from '@api/common/network/network-summary';
import { ApiResponse } from '@api/custom/api-response';
import { routerNavigationAction } from '@ngrx/router-store';
import { on } from '@ngrx/store';
import { createReducer } from '@ngrx/store';
import { RoutingUtil } from '@app/base/routing-util';
import { actionNetworkMapPageLoad } from './network.actions';
import { actionNetworkRoutesPageLoad } from './network.actions';
import { actionNetworkNodesPageLoad } from './network.actions';
import { actionNetworkFactsPageLoad } from './network.actions';
import { actionNetworkDetailsPageLoad } from './network.actions';
import { actionNetworkChangesPageInit } from './network.actions';
import { actionNetworkChangesLoad } from './network.actions';
import { actionNetworkChangesPageSize } from './network.actions';
import { actionNetworkChangesImpact } from './network.actions';
import { actionNetworkLink } from './network.actions';
import { actionNetworkChangesFilterOption } from './network.actions';
import { actionNetworkChangesPageIndex } from './network.actions';
import { actionNetworkDetailsPageLoaded } from './network.actions';
import { actionNetworkNodesPageLoaded } from './network.actions';
import { actionNetworkRoutesPageLoaded } from './network.actions';
import { actionNetworkFactsPageLoaded } from './network.actions';
import { actionNetworkMapPageLoaded } from './network.actions';
import { actionNetworkChangesPageLoaded } from './network.actions';
import { NetworkState } from './network.state';
import { initialState } from './network.state';

const defaultSummary: NetworkSummary = {
  name: '',
  networkType: null,
  networkScope: null,
  factCount: 0,
  nodeCount: 0,
  routeCount: 0,
  changeCount: 0,
};

export const networkReducer = createReducer<NetworkState>(
  initialState,
  on(routerNavigationAction, (state, action): NetworkState => {
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
  on(
    actionNetworkDetailsPageLoad,
    actionNetworkFactsPageLoad,
    actionNetworkNodesPageLoad,
    actionNetworkRoutesPageLoad,
    actionNetworkMapPageLoad,
    (state, { networkId }): NetworkState => {
      if (networkId === state.networkId) {
        return state;
      }
      return {
        ...state,
        networkId,
        summary: defaultSummary,
      };
    }
  ),
  on(
    actionNetworkLink,
    (state, { networkId, networkName, networkType }): NetworkState => {
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
    }
  ),
  on(
    actionNetworkDetailsPageLoaded,
    (state, response): NetworkState => ({
      ...state,
      summary: response.result?.summary ?? defaultSummary,
      detailsPage: response,
    })
  ),
  on(
    actionNetworkNodesPageLoaded,
    (state, response): NetworkState => ({
      ...state,
      summary: response.result?.summary ?? defaultSummary,
      nodesPage: response,
    })
  ),
  on(
    actionNetworkRoutesPageLoaded,
    (state, response): NetworkState => ({
      ...state,
      summary: response.result?.summary ?? defaultSummary,
      routesPage: response,
    })
  ),
  on(
    actionNetworkFactsPageLoaded,
    (state, response): NetworkState => ({
      ...state,
      summary: response.result?.summary ?? defaultSummary,
      factsPage: response,
    })
  ),
  on(
    actionNetworkMapPageLoaded,
    (state, { response, mapPositionFromUrl }): NetworkState => ({
      ...state,
      summary: response.result?.summary ?? defaultSummary,
      mapPage: response,
      mapPositionFromUrl,
    })
  ),
  on(
    actionNetworkChangesPageInit,
    (state): NetworkState => ({
      ...state,
      changesParameters: {
        ...state.changesParameters,
        pageIndex: 0,
      },
    })
  ),
  on(
    actionNetworkChangesLoad,
    (state, { networkId, changesParameters }): NetworkState => {
      return {
        ...state,
        networkId,
        summary: defaultSummary,
        changesParameters,
      };
    }
  ),
  on(
    actionNetworkChangesPageLoaded,
    (state, response): NetworkState => ({
      ...state,
      summary: response.result?.network ?? defaultSummary,
      changesPage: response,
    })
  ),
  on(
    actionNetworkChangesImpact,
    (state, action): NetworkState => ({
      ...state,
      changesParameters: {
        ...state.changesParameters,
        impact: action.impact,
        pageIndex: 0,
      },
    })
  ),
  on(
    actionNetworkChangesPageSize,
    (state, action): NetworkState => ({
      ...state,
      changesParameters: {
        ...state.changesParameters,
        pageSize: action.pageSize,
        pageIndex: 0,
      },
    })
  ),
  on(
    actionNetworkChangesPageIndex,
    (state, action): NetworkState => ({
      ...state,
      changesParameters: {
        ...state.changesParameters,
        pageIndex: action.pageIndex,
      },
    })
  ),
  on(
    actionNetworkChangesFilterOption,
    (state, action): NetworkState => ({
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
