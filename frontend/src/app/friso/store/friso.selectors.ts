import { createFeatureSelector } from '@ngrx/store';
import { createSelector } from '@ngrx/store';
import { frisoFeatureKey, FrisoState } from './friso.state';

export const selectFrisoState = createFeatureSelector<FrisoState>(frisoFeatureKey);

export const selectFrisoMode = createSelector(selectFrisoState, (state: FrisoState) => state.mode);
