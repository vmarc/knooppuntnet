import { routerNavigatedAction } from '@ngrx/router-store';
import { createReducer } from '@ngrx/store';
import { on } from '@ngrx/store';
import { actionChangesAnalysisStrategy } from '../../analysis/changes/store/changes.actions';
import { actionChangesPageSize } from '../../analysis/changes/store/changes.actions';
import { actionChangesImpact } from '../../analysis/changes/store/changes.actions';
import { actionLocationRoutesPageSize } from '../../analysis/location/store/location.actions';
import { actionLocationNodesPageSize } from '../../analysis/location/store/location.actions';
import { actionLocationSelectionPageInit } from '../../analysis/location/store/location.actions';
import { actionLocationSelectionPageStrategy } from '../../analysis/location/store/location.actions';
import { actionNetworkChangesPageSize } from '../../analysis/network/store/network.actions';
import { actionNetworkChangesImpact } from '../../analysis/network/store/network.actions';
import { actionNodeChangesPageSize } from '../../analysis/node/store/node.actions';
import { actionNodeChangesPageLoad } from '../../analysis/node/store/node.actions';
import { actionNodeChangesFilterOption } from '../../analysis/node/store/node.actions';
import { actionRouteChangesPageSize } from '../../analysis/route/store/route.actions';
import { actionRouteChangesFilterOption } from '../../analysis/route/store/route.actions';
import { actionRouteChangesPageLoaded } from '../../analysis/route/store/route.actions';
import { actionRouteMapPageLoaded } from '../../analysis/route/store/route.actions';
import { actionRouteDetailsPageLoaded } from '../../analysis/route/store/route.actions';
import { actionSubsetChangesPageSize } from '../../analysis/subset/store/subset.actions';
import { actionSubsetChangesPageImpact } from '../../analysis/subset/store/subset.actions';
import { Util } from '../../components/shared/util';
import { actionPreferencesAnalysisStrategy } from './preferences.actions';
import { actionPreferencesShowOptions } from './preferences.actions';
import { actionPreferencesShowLegend } from './preferences.actions';
import { actionPreferencesShowAppearanceOptions } from './preferences.actions';
import { actionPreferencesPlanProposed } from './preferences.actions';
import { actionPreferencesShowProposed } from './preferences.actions';
import { actionPreferencesImpact } from './preferences.actions';
import { actionPreferencesPageSize } from './preferences.actions';
import { actionPreferencesNetworkType } from './preferences.actions';
import { actionPreferencesInstructions } from './preferences.actions';
import { actionPreferencesExtraLayers } from './preferences.actions';
import { AnalysisStrategy } from './preferences.state';
import { initialState } from './preferences.state';

export const preferencesReducer = createReducer<PreferencesState>(
  initialState,
  on(routerNavigatedAction, (state, action): PreferencesState => {
    if (action.payload.routerState.url.includes('/analysis/changes')) {
      const queryParams = action.payload.routerState.root.queryParams;
      const pageSize = +queryParams['pageSize'];
      const impact = queryParams['impact'];
      let strategy: AnalysisStrategy;
      if ('network' === queryParams['strategy']) {
        strategy = AnalysisStrategy.network;
      } else if ('location' === queryParams['strategy']) {
        strategy = AnalysisStrategy.location;
      }
      return {
        ...state,
        strategy: strategy ?? state.strategy,
        pageSize: pageSize ?? state.pageSize,
        impact: impact ?? state.impact,
      };
    } else {
      const params = Util.paramsIn(action.payload.routerState.root);
      const networkType = params.get('networkType');
      if (networkType) {
        return { ...state, networkType };
      }
      return state;
    }
  }),
  on(
    actionPreferencesAnalysisStrategy,
    actionChangesAnalysisStrategy,
    actionLocationSelectionPageStrategy,
    (state, action): PreferencesState => ({
      ...state,
      strategy: action.strategy,
    })
  ),
  on(
    actionLocationSelectionPageInit,
    (state): PreferencesState => ({
      ...state,
      strategy: AnalysisStrategy.location,
    })
  ),
  on(
    actionPreferencesNetworkType,
    (state, action): PreferencesState => ({
      ...state,
      networkType: action.networkType,
    })
  ),
  on(
    actionPreferencesInstructions,
    (state, action): PreferencesState => ({
      ...state,
      instructions: action.instructions,
    })
  ),
  on(
    actionPreferencesExtraLayers,
    (state, action): PreferencesState => ({
      ...state,
      extraLayers: action.extraLayers,
    })
  ),
  on(
    actionPreferencesPageSize,
    actionNetworkChangesPageSize,
    actionChangesPageSize,
    actionSubsetChangesPageSize,
    actionLocationNodesPageSize,
    actionLocationRoutesPageSize,
    actionNodeChangesPageSize,
    actionRouteChangesPageSize,
    (state, action): PreferencesState => ({
      ...state,
      pageSize: action.pageSize,
    })
  ),
  on(
    actionNodeChangesPageLoad,
    (state, action): PreferencesState => ({
      ...state,
      pageSize: action.changesParameters.pageSize,
    })
  ),
  on(
    actionPreferencesImpact,
    (state, action): PreferencesState => ({
      ...state,
      impact: action.impact,
    })
  ),
  on(
    actionPreferencesShowAppearanceOptions,
    (state, action): PreferencesState => ({
      ...state,
      showAppearanceOptions: action.value,
    })
  ),
  on(
    actionPreferencesShowLegend,
    (state, action): PreferencesState => ({
      ...state,
      showLegend: action.value,
    })
  ),
  on(
    actionPreferencesShowOptions,
    (state, action): PreferencesState => ({
      ...state,
      showOptions: action.value,
    })
  ),
  on(
    actionPreferencesShowProposed,
    (state, action): PreferencesState => ({
      ...state,
      showProposed: action.value,
    })
  ),
  on(
    actionPreferencesPlanProposed,
    (state, action): PreferencesState => ({
      ...state,
      planProposed: action.value,
    })
  ),
  on(actionRouteDetailsPageLoaded, (state, response): PreferencesState => {
    const networkType =
      response?.result?.route.summary.networkType ?? state.networkType;
    return {
      ...state,
      networkType,
    };
  }),
  on(actionRouteMapPageLoaded, (state, { response }): PreferencesState => {
    const networkType =
      response?.result?.routeMapInfo.networkType ?? state.networkType;
    return {
      ...state,
      networkType,
    };
  }),
  on(actionRouteChangesPageLoaded, (state, response): PreferencesState => {
    const networkType =
      response?.result?.routeNameInfo.networkType ?? state.networkType;
    return {
      ...state,
      networkType,
    };
  }),
  on(
    actionNodeChangesFilterOption,
    (state, { option }): PreferencesState => ({
      ...state,
      impact: option.impact,
    })
  ),
  on(
    actionRouteChangesFilterOption,
    (state, { option }): PreferencesState => ({
      ...state,
      impact: option.impact,
    })
  ),
  on(
    actionNetworkChangesImpact,
    (state, { impact }): PreferencesState => ({
      ...state,
      impact,
    })
  ),
  on(
    actionChangesImpact,
    (state, { impact }): PreferencesState => ({
      ...state,
      impact,
    })
  ),
  on(
    actionSubsetChangesPageImpact,
    (state, { impact }): PreferencesState => ({
      ...state,
      impact,
    })
  )
);
