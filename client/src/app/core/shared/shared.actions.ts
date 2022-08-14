import { createAction } from '@ngrx/store';
import { props } from '@ngrx/store';
import { EditParameters } from '../../analysis/components/edit/edit-parameters';

export const actionSharedHttpError = createAction(
  '[Shared] Http error',
  props<{ httpError: string }>()
);

export const actionSharedEdit = createAction(
  '[Shared] Edit',
  props<{ editParameters: EditParameters }>()
);

export const actionSharedLanguage = createAction(
  '[Shared] Language',
  props<{ language: string }>()
);
