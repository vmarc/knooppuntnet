import {createSelector} from '@ngrx/store';
import {selectPreferencesState} from '../core.state';
import {PreferencesState} from './preferences.model';

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
