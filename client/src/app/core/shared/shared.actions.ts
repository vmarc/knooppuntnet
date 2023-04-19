import { EditParameters } from '@app/analysis/components/edit';
import { createAction } from '@ngrx/store';
import { props } from '@ngrx/store';

export const actionSharedHttpError = createAction(
  '[Shared] Http error',
  props<{ httpError: string }>()
);

export const actionSharedEdit = createAction(
  '[Shared] Edit',
  props<EditParameters>()
);
