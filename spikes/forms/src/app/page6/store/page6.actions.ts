import { props } from '@ngrx/store';
import { createAction } from '@ngrx/store';

export const actionPage6a = createAction(
  '[Page6Component] a',
  props<{ admin: boolean }>()
);

export const actionPage6b = createAction('[Page6Component] b');
