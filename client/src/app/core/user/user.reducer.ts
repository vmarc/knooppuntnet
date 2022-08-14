import { on } from '@ngrx/store';
import { createReducer } from '@ngrx/store';
import { actionUserReceived } from './user.actions';
import { actionUserLoginCallbackPageRegistered } from './user.actions';
import { actionUserSet } from './user.actions';
import { initialUserState } from './user.state';

export const userReducer = createReducer(
  initialUserState,
  on(actionUserSet, (state, { user }) => ({
    ...state,
    user,
  })),
  on(actionUserLoginCallbackPageRegistered, (state, { loginCallbackPage }) => ({
    ...state,
    loginCallbackPage,
  })),
  on(actionUserReceived, (state, { user }) => ({
    ...state,
    user,
  }))
);
