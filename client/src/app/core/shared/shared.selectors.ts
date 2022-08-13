import { createSelector } from '@ngrx/store';
import { selectSharedState } from '../core.state';
import { SharedState } from './shared.state';

export const selectSharedHttpError = createSelector(
  selectSharedState,
  (state: SharedState) => state.httpError
);

export const selectSharedUser = createSelector(
  selectSharedState,
  (state: SharedState) => state.user
);

export const selectSharedLoggedIn = createSelector(
  selectSharedState,
  (state: SharedState) => !!state.user
);

export const selectSharedLanguage = createSelector(
  selectSharedState,
  (state: SharedState) => state.language
);

export const selectSharedLoginCallbackPage = createSelector(
  selectSharedState,
  (state: SharedState) => state.loginCallbackPage
);
