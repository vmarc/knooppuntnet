import { on } from '@ngrx/store';
import { createReducer } from '@ngrx/store';
import { actionUserLoginCompleted } from './user.actions';
import { actionUserLogoutCompleted } from './user.actions';
import { actionUserLogoutReturnUrlRegistered } from './user.actions';
import { actionUserInit } from './user.actions';
import { actionUserLoggedOut } from './user.actions';
import { actionUserReceived } from './user.actions';
import { actionUserLoginReturnUrlRegistered } from './user.actions';
import { UserState } from './user.state';
import { initialUserState } from './user.state';

export const userReducer = createReducer<UserState>(
  initialUserState,
  on(
    actionUserInit,
    (state, { user }): UserState => ({
      ...state,
      user,
    })
  ),
  on(
    actionUserLoginReturnUrlRegistered,
    actionUserLogoutReturnUrlRegistered,
    (state, { returnUrl }): UserState => ({
      ...state,
      returnUrl,
    })
  ),
  on(
    actionUserLoginCompleted,
    actionUserLogoutCompleted,
    (state): UserState => ({
      ...state,
      returnUrl: null,
    })
  ),
  on(
    actionUserReceived,
    (state, { user, returnUrl }): UserState => ({
      ...state,
      user,
      returnUrl,
    })
  ),
  on(
    actionUserLoggedOut,
    (state): UserState => ({
      ...state,
      user: null,
    })
  )
);
