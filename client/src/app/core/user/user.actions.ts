import { createAction } from '@ngrx/store';
import { props } from '@ngrx/store';

export const actionUserInit = createAction(
  '[User] Init',
  props<{ user: string }>()
);

export const actionUserSet = createAction(
  '[User] Set',
  props<{ user: string }>()
);

export const actionUserLogin = createAction('[User] Login');

export const actionUserLogout = createAction('[User] Logout');

export const actionUserAuthenticated = createAction('[User] Authenticated');

export const actionUserLoginLinkClicked = createAction(
  '[User] Login link clicked'
);

export const actionUserLoginCallbackPageRegistered = createAction(
  '[User] Login callback page registered',
  props<{ loginCallbackPage: string }>()
);

export const actionUserReceived = createAction(
  '[User] Received',
  props<{ user: string; pageArray: string[] }>()
);
