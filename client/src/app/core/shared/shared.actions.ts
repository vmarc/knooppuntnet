import { createAction } from '@ngrx/store';
import { props } from '@ngrx/store';
import { EditParameters } from '../../analysis/components/edit/edit-parameters';

export const actionSharedHttpError = createAction(
  '[HttpInterceptor] Error',
  props<{ httpError: string }>()
);

export const actionSharedEdit = createAction(
  '[Edit] Edit',
  props<{ editParameters: EditParameters }>()
);
