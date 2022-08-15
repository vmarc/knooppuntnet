import { on } from '@ngrx/store';
import { createReducer } from '@ngrx/store';
import { actionUserLoginCompleted } from './user.actions';
import { actionUserLogoutCompleted } from './user.actions';
import { actionUserLogoutReturnUrlRegistered } from './user.actions';
import { actionUserInit } from './user.actions';
import { actionUserLoggedOut } from './user.actions';
import { actionUserReceived } from './user.actions';
import { actionUserLoginReturnUrlRegistered } from './user.actions';
import { initialUserState } from './user.state';

export const userReducer = createReducer(
  initialUserState,
  on(actionUserInit, (state, { user }) => ({
    ...state,
    user,
  })),
  on(
    actionUserLoginReturnUrlRegistered,
    actionUserLogoutReturnUrlRegistered,
    (state, { returnUrl }) => ({
      ...state,
      returnUrl,
    })
  ),
  on(actionUserLoginCompleted, actionUserLogoutCompleted, (state, {}) => ({
    ...state,
    returnUrl: null,
  })),
  on(actionUserReceived, (state, { user, returnUrl }) => ({
    ...state,
    user,
    returnUrl,
  })),
  on(actionUserLoggedOut, (state, {}) => ({
    ...state,
    user: null,
  }))
);
