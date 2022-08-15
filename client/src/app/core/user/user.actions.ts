import { createAction } from '@ngrx/store';
import { props } from '@ngrx/store';

export const actionUserInit = createAction(
  '[User] Init',
  props<{ user: string }>()
);

export const actionUserLogin = createAction('[User] Login');

export const actionUserLogout = createAction('[User] Logout');

export const actionUserAuthenticated = createAction('[User] Authenticated');

export const actionUserLoginLinkClicked = createAction(
  '[User] Login link clicked'
);

export const actionUserLoginReturnUrlRegistered = createAction(
  '[User] Login return url registered',
  props<{ returnUrl: string }>()
);

export const actionUserReceived = createAction(
  '[User] Received',
  props<{ user: string; returnUrl: string }>()
);

export const actionUserLoginCompleted = createAction('[User] Login completed');

export const actionUserLogoutLinkClicked = createAction(
  '[User] Logout link clicked'
);

export const actionUserLogoutReturnUrlRegistered = createAction(
  '[User] Logout return url registered',
  props<{ returnUrl: string }>()
);

export const actionUserLoggedOut = createAction('[User] Logged out');

export const actionUserLogoutCompleted = createAction(
  '[User] Logout completed'
);
