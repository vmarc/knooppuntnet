import { createFeatureSelector } from '@ngrx/store';
import { createSelector } from '@ngrx/store';
import { SharedState } from './shared.state';

export const selectSharedState = createFeatureSelector<SharedState>('shared');

export const selectSharedHttpError = createSelector(
  selectSharedState,
  (state: SharedState) => state.httpError
);

export const selectSharedSurveyDateInfo = createSelector(
  selectSharedState,
  (state: SharedState) => state.surveyDateInfo
);
