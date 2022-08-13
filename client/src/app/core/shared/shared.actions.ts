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

export const actionSharedInitUser = createAction(
  '[Shared] Init user',
  props<{ user: string }>()
);

export const actionSharedUser = createAction(
  '[Shared] User',
  props<{ user: string }>()
);

export const actionSharedLogin = createAction('[Shared] Login');

export const actionSharedLogout = createAction('[Shared] Logout');

export const actionSharedAuthenticated = createAction('[Shared] Authenticated');

export const actionSharedRegisterLoginCallbackPage = createAction(
  '[Shared] Register login callback page'
);

export const actionSharedLoginCallbackPageRegistered = createAction(
  '[Shared] Login callback page registered',
  props<{ loginCallbackPage: string }>()
);

export const actionSharedUserReceived = createAction(
  '[Shared] User received',
  props<{ user: string; pageArray: string[] }>()
);
