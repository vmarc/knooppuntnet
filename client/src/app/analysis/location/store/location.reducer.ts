import { LocationKey } from '@api/custom/location-key';
import { LocationNodesType } from '@api/custom/location-nodes-type';
import { LocationRoutesType } from '@api/custom/location-routes-type';
import { routerNavigatedAction } from '@ngrx/router-store';
import { routerNavigationAction } from '@ngrx/router-store';
import { on } from '@ngrx/store';
import { createReducer } from '@ngrx/store';
import { Util } from '../../../components/shared/util';
import { Countries } from '../../../kpn/common/countries';
import { NetworkTypes } from '../../../kpn/common/network-types';
import { actionLocationRoutesPageSize } from './location.actions';
import { actionLocationNodesPageSize } from './location.actions';
import { actionLocationNodesPageInit } from './location.actions';
import { actionLocationRoutesPageInit } from './location.actions';
import { actionLocationRoutesPageIndex } from './location.actions';
import { actionLocationRoutesType } from './location.actions';
import { actionLocationNodesPageIndex } from './location.actions';
import { actionLocationNodesType } from './location.actions';
import { actionLocationNodesPageLoaded } from './location.actions';
import { actionLocationRoutesPageLoaded } from './location.actions';
import { actionLocationFactsPageLoaded } from './location.actions';
import { actionLocationMapPageLoaded } from './location.actions';
import { actionLocationChangesPageLoaded } from './location.actions';
import { actionLocationEditPageLoaded } from './location.actions';
import { initialState } from './location.state';

export const locationReducer = createReducer(
  initialState,
  on(routerNavigationAction, (state, action) => ({
    ...state,
    nodesPage: null,
    routesPage: null,
    factsPage: null,
    mapPage: null,
    changesPage: null,
    editPage: null,
  })),
  on(routerNavigatedAction, (state, action) => {
    const params = Util.paramsIn(action.payload.routerState.root);
    const networkType = NetworkTypes.withName(params.get('networkType'));
    const country = Countries.withDomain(params.get('country'));
    const name = params.get('location');
    if (networkType && country && name) {
      const locationKey: LocationKey = {
        networkType,
        country,
        name,
      };
      return { ...state, locationKey };
    }
    return state;
  }),
  on(actionLocationNodesPageInit, (state, {}) => ({
    ...state,
    nodesPageType: LocationNodesType.all,
    nodesPageIndex: 0,
  })),
  on(actionLocationNodesType, (state, { locationNodesType }) => ({
    ...state,
    nodesPageType: locationNodesType,
    nodesPageIndex: 0,
  })),
  on(actionLocationNodesPageSize, (state, {}) => ({
    ...state,
    nodesPageIndex: 0,
  })),
  on(actionLocationNodesPageIndex, (state, { pageIndex }) => ({
    ...state,
    nodesPageIndex: pageIndex,
  })),
  on(actionLocationNodesPageLoaded, (state, { response }) => ({
    ...state,
    nodesPage: response,
    locationSummary: response.result?.summary,
  })),
  on(actionLocationRoutesPageInit, (state, {}) => ({
    ...state,
    routesPageType: LocationRoutesType.all,
    routesPageIndex: 0,
  })),
  on(actionLocationRoutesType, (state, { locationRoutesType }) => ({
    ...state,
    routesPageType: locationRoutesType,
    routesPageIndex: 0,
  })),
  on(actionLocationRoutesPageSize, (state, {}) => ({
    ...state,
    routesPageIndex: 0,
  })),
  on(actionLocationRoutesPageIndex, (state, { pageIndex }) => ({
    ...state,
    routesPageIndex: pageIndex,
  })),
  on(actionLocationRoutesPageLoaded, (state, { response }) => ({
    ...state,
    routesPage: response,
    locationSummary: response.result?.summary,
  })),
  on(actionLocationFactsPageLoaded, (state, { response }) => ({
    ...state,
    factsPage: response,
    locationSummary: response.result?.summary,
  })),
  on(actionLocationMapPageLoaded, (state, { response }) => ({
    ...state,
    mapPage: response,
    locationSummary: response.result?.summary,
  })),
  on(actionLocationChangesPageLoaded, (state, { response }) => ({
    ...state,
    changesPage: response,
    locationSummary: response.result?.summary,
  })),
  on(actionLocationEditPageLoaded, (state, { response }) => ({
    ...state,
    editPage: response,
    locationSummary: response.result?.summary,
  }))
);
