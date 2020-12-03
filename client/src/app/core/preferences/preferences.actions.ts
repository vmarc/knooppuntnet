import {createAction} from '@ngrx/store';
import {props} from '@ngrx/store';

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
