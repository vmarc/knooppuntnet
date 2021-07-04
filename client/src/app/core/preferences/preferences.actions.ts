import { createAction } from '@ngrx/store';
import { props } from '@ngrx/store';

export const actionPreferencesNetworkType = createAction(
  '[Preferences] NetworkType',
  props<{ networkType: string }>()
);

export const actionPreferencesInstructions = createAction(
  '[Preferences] Instructions',
  props<{ instructions: boolean }>()
);

export const actionPreferencesExtraLayers = createAction(
  '[Preferences] Extra layers',
  props<{ extraLayers: boolean }>()
);

export const actionPreferencesItemsPerPage = createAction(
  '[Preferences] Items per page',
  props<{ itemsPerPage: number }>()
);

export const actionPreferencesImpact = createAction(
  '[Preferences] Impact',
  props<{ impact: boolean }>()
);

export const actionPreferencesShowAppearanceOptions = createAction(
  '[Preferences] Show appearance options',
  props<{ value: boolean }>()
);

export const actionPreferencesShowLegend = createAction(
  '[Preferences] Show legend',
  props<{ value: boolean }>()
);

export const actionPreferencesShowOptions = createAction(
  '[Preferences] Show options',
  props<{ value: boolean }>()
);

export const actionPreferencesShowProposed = createAction(
  '[Preferences] Show proposed',
  props<{ value: boolean }>()
);

export const actionPreferencesPlanProposed = createAction(
  '[Preferences] Plan proposed',
  props<{ value: boolean }>()
);
