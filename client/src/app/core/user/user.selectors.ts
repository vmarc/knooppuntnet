import { createSelector } from '@ngrx/store';
import { selectUserState } from '../core.state';
import { UserState } from './user.state';

export const selectUserUser = createSelector(
  selectUserState,
  (state: UserState) => state.user
);

export const selectUserLoggedIn = createSelector(
  selectUserState,
  (state: UserState) => !!state.user
);

export const selectUserLoginCallbackPage = createSelector(
  selectUserState,
  (state: UserState) => state.loginCallbackPage
);
