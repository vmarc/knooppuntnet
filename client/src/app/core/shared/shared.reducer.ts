import { routerNavigationAction } from '@ngrx/router-store';
import { on } from '@ngrx/store';
import { createReducer } from '@ngrx/store';
import { actionSharedUserReceived } from './shared.actions';
import { actionSharedLoginCallbackPageRegistered } from './shared.actions';
import { actionSharedUser } from './shared.actions';
import { actionSharedLanguage } from './shared.actions';
import { actionSharedHttpError } from './shared.actions';
import { initialSharedState } from './shared.state';

export const sharedReducer = createReducer(
  initialSharedState,
  on(routerNavigationAction, (state, action) => ({
    ...state,
    httpError: null,
  })),
  on(actionSharedHttpError, (state, { httpError }) => ({
    ...state,
    httpError,
  })),
  on(actionSharedLanguage, (state, { language }) => ({
    ...state,
    language,
  })),
  on(actionSharedUser, (state, { user }) => ({
    ...state,
    user,
  })),
  on(
    actionSharedLoginCallbackPageRegistered,
    (state, { loginCallbackPage }) => ({
      ...state,
      loginCallbackPage,
    })
  ),
  on(actionSharedUserReceived, (state, { user }) => ({
    ...state,
    user,
  }))
);
