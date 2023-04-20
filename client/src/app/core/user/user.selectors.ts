import { createFeatureSelector } from '@ngrx/store';
import { createSelector } from '@ngrx/store';
import { UserState } from './user.state';

export const selectUserState = createFeatureSelector<UserState>('user');

export const selectUserUser = createSelector(
  selectUserState,
  (state: UserState) => state.user
);

export const selectUserLoggedIn = createSelector(
  selectUserState,
  (state: UserState) => !!state.user
);

export const selectUserReturnUrl = createSelector(
  selectUserState,
  (state: UserState) => state.returnUrl
);
