import { routerNavigatedAction } from '@ngrx/router-store';
import { createReducer } from '@ngrx/store';
import { on } from '@ngrx/store';
import { actionRouteChangesPageLoaded } from '../../analysis/route/store/route.actions';
import { actionRouteMapPageLoaded } from '../../analysis/route/store/route.actions';
import { actionRouteDetailsPageLoaded } from '../../analysis/route/store/route.actions';
import { Util } from '../../components/shared/util';
import { actionPreferencesImpact } from './preferences.actions';
import { actionPreferencesItemsPerPage } from './preferences.actions';
import { actionPreferencesNetworkType } from './preferences.actions';
import { actionPreferencesInstructions } from './preferences.actions';
import { actionPreferencesExtraLayers } from './preferences.actions';
import { initialState } from './preferences.state';

export const preferencesReducer = createReducer(
  initialState,
  on(routerNavigatedAction, (state, action) => {
    const params = Util.paramsIn(action.payload.routerState.root);
    const networkType = params.get('networkType');
    if (networkType) {
      return { ...state, networkType };
    }
    return state;
  }),
  on(actionPreferencesNetworkType, (state, action) => ({
    ...state,
    networkType: action.networkType,
  })),
  on(actionPreferencesInstructions, (state, action) => ({
    ...state,
    instructions: action.instructions,
  })),
  on(actionPreferencesExtraLayers, (state, action) => ({
    ...state,
    extraLayers: action.extraLayers,
  })),
  on(actionPreferencesItemsPerPage, (state, action) => ({
    ...state,
    itemsPerPage: action.itemsPerPage,
  })),
  on(actionPreferencesImpact, (state, action) => ({
    ...state,
    impact: action.impact,
  })),
  on(actionRouteDetailsPageLoaded, (state, { response }) => {
    const networkType =
      response?.result.route.summary.networkType ?? state.networkType;
    return {
      ...state,
      networkType,
    };
  }),
  on(actionRouteMapPageLoaded, (state, { response }) => {
    const networkType =
      response?.result.routeMapInfo.networkType ?? state.networkType;
    return {
      ...state,
      networkType,
    };
  }),
  on(actionRouteChangesPageLoaded, (state, { response }) => {
    const networkType =
      response?.result.routeNameInfo.networkType ?? state.networkType;
    return {
      ...state,
      networkType,
    };
  })
);
