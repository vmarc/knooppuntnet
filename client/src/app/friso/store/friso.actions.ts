import { props } from '@ngrx/store';
import { createAction } from '@ngrx/store';

export const actionFrisoPageInit = createAction('[FrisoPage] Init');

export const actionFrisoPageLoad = createAction(
  '[FrisoPage] Load',
  props<{ mode: string }>()
);

export const actionFrisoPageLoaded = createAction('[FrisoPage] Loaded');

export const actionFrisoMode = createAction(
  '[Friso] Mode',
  props<{ mode: string }>()
);
