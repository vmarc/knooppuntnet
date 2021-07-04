import {createSelector} from '@ngrx/store';
import {selectPreferencesState} from '../core.state';
import {PreferencesState} from './preferences.state';

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

export const selectPreferencesItemsPerPage = createSelector(
  selectPreferencesState,
  (state: PreferencesState) => state.itemsPerPage
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
