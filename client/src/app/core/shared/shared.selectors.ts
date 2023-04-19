import { selectSharedState } from '@app/core';
import { createSelector } from '@ngrx/store';
import { SharedState } from './shared.state';

export const selectSharedHttpError = createSelector(
  selectSharedState,
  (state: SharedState) => state.httpError
);
