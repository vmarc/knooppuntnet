import { Util } from '@app/components/shared';
import { routerNavigatedAction } from '@ngrx/router-store';
import { createReducer } from '@ngrx/store';
import { on } from '@ngrx/store';
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
    (state, action): PreferencesState => ({
      ...state,
      strategy: action.strategy,
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
    (state, action): PreferencesState => ({
      ...state,
      pageSize: action.pageSize,
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
  )
);
