import { createSelector } from '@ngrx/store';
import { selectUserState } from '@app/core/core.state';
import { UserState } from './user.state';

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
