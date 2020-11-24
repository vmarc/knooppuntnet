import {createAction} from '@ngrx/store';
import {props} from '@ngrx/store';

export const networkType = createAction(
  '[Preferences] NetworkType',
  props<{ networkType: string }>()
);

export const instructions = createAction(
  '[Preferences] Instructions',
  props<{ instructions: boolean }>()
);

export const extraLayers = createAction(
  '[Preferences] Extra layers',
  props<{ extraLayers: boolean }>()
);
