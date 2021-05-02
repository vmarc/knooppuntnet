import { Country } from '@api/custom/country';
import { LocationKey } from '@api/custom/location-key';
import { NetworkType } from '@api/custom/network-type';
import { routerNavigatedAction } from '@ngrx/router-store';
import { routerNavigationAction } from '@ngrx/router-store';
import { on } from '@ngrx/store';
import { createReducer } from '@ngrx/store';
import { Util } from '../../../components/shared/util';
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
    locationKey: null,
    locationSummary: null,
    nodesPage: null,
    routesPage: null,
    factsPage: null,
    mapPage: null,
    changesPage: null,
    editPage: null,
  })),
  on(routerNavigatedAction, (state, action) => {
    const params = Util.paramsIn(action.payload.routerState.root);
    const networkType = params.get('networkType');
    const country = params.get('country');
    const name = params.get('location');
    if (networkType && country && name) {
      const locationKey = new LocationKey(
        networkType as NetworkType,
        country as Country,
        name
      );
      return { ...state, locationKey };
    }
    return state;
  }),
  on(actionLocationNodesType, (state, { locationNodesType }) => ({
    ...state,
    nodesPageType: locationNodesType,
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
