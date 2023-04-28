import { createFeatureSelector } from '@ngrx/store';
import { createSelector } from '@ngrx/store';
import { PreferencesState } from './preferences.state';

export const selectPreferencesState =
  createFeatureSelector<PreferencesState>('preferences');

export const selectPreferencesAnalysisStrategy = createSelector(
  selectPreferencesState,
  (state: PreferencesState) => state.strategy
);

export const selectPreferencesNetworkType = createSelector(
  selectPreferencesState,
  (state: PreferencesState) => state.networkType
);

export const selectPreferencesInstructions = createSelector(
  selectPreferencesState,
  (state: PreferencesState) => state.instructions
);

export const selectPreferencesExtraLayers = createSelector(
  selectPreferencesState,
  (state: PreferencesState) => state.extraLayers
);

export const selectPreferencesPageSize = createSelector(
  selectPreferencesState,
  (state: PreferencesState) => state.pageSize
);

export const selectPreferencesImpact = createSelector(
  selectPreferencesState,
  (state: PreferencesState) => state.impact
);

export const selectPreferencesShowAppearanceOptions = createSelector(
  selectPreferencesState,
  (state: PreferencesState) => state.showAppearanceOptions
);

export const selectPreferencesShowLegend = createSelector(
  selectPreferencesState,
  (state: PreferencesState) => state.showLegend
);

export const selectPreferencesShowOptions = createSelector(
  selectPreferencesState,
  (state: PreferencesState) => state.showOptions
);

export const selectPreferencesShowProposed = createSelector(
  selectPreferencesState,
  (state: PreferencesState) => state.showProposed
);

export const selectPreferencesPlanProposed = createSelector(
  selectPreferencesState,
  (state: PreferencesState) => state.planProposed
);
