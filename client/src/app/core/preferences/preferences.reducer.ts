import { routerNavigatedAction } from '@ngrx/router-store';
import { createReducer } from '@ngrx/store';
import { on } from '@ngrx/store';
import { actionChangesAnalysisStrategy } from '@app/analysis/changes/store/changes.actions';
import { actionChangesPageSize } from '@app/analysis/changes/store/changes.actions';
import { actionChangesImpact } from '@app/analysis/changes/store/changes.actions';
import { actionLocationRoutesPageSize } from '@app/analysis/location/store/location.actions';
import { actionLocationNodesPageSize } from '@app/analysis/location/store/location.actions';
import { actionLocationSelectionPageInit } from '@app/analysis/location/store/location.actions';
import { actionLocationSelectionPageStrategy } from '@app/analysis/location/store/location.actions';
import { actionNetworkChangesPageSize } from '@app/analysis/network/store/network.actions';
import { actionNetworkChangesImpact } from '@app/analysis/network/store/network.actions';
import { actionNodeChangesPageSize } from '@app/analysis/node/store/node.actions';
import { actionNodeChangesPageLoad } from '@app/analysis/node/store/node.actions';
import { actionNodeChangesFilterOption } from '@app/analysis/node/store/node.actions';
import { actionRouteChangesPageSize } from '@app/analysis/route/store/route.actions';
import { actionRouteChangesFilterOption } from '@app/analysis/route/store/route.actions';
import { actionRouteChangesPageLoaded } from '@app/analysis/route/store/route.actions';
import { actionRouteMapPageLoaded } from '@app/analysis/route/store/route.actions';
import { actionRouteDetailsPageLoaded } from '@app/analysis/route/store/route.actions';
import { actionSubsetChangesPageSize } from '@app/analysis/subset/store/subset.actions';
import { actionSubsetChangesPageImpact } from '@app/analysis/subset/store/subset.actions';
import { Util } from '@app/components/shared/util';
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
import { PreferencesState } from './preferences.state';
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
