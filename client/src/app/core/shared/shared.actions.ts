import {createAction} from '@ngrx/store';
import {props} from '@ngrx/store';

export const actionSharedHttpError = createAction(
  "[HttpInterceptor] Error",
  props<{ httpError: string }>()
);
