import { FrisoNode } from '@app/friso/friso/friso-node';
import { props } from '@ngrx/store';
import { createAction } from '@ngrx/store';

export const actionFrisoMapInitialized = createAction(
  '[FrisoPage] Load',
  props<{ mode: string }>()
);

export const actionFrisoMapViewInit = createAction('[Friso] Map view init');

export const actionFrisoMode = createAction(
  '[Friso] Mode',
  props<{ mode: string }>()
);

export const actionFrisoNodeClicked = createAction(
  '[Friso] Node clicked',
  props<{ node: FrisoNode }>()
);
