import { createAction } from '@ngrx/store';
import { props } from '@ngrx/store';
import { SurveyDateValues } from './survey-date-values';

export const actionSharedHttpError = createAction(
  '[Shared] Http error',
  props<{ httpError: string }>()
);

export const actionSharedSurveyDateInfoInit = createAction(
  '[Shared] SurveyDateInfo init'
);

export const actionSharedSurveyDateInfoLoaded = createAction(
  '[Shared] SurveyDateInfo loaded',
  props<{ surveyDateInfo: SurveyDateValues }>()
);

export const actionSharedSurveyDateInfoAlreadyLoaded = createAction(
  '[Shared] SurveyDateInfo already loaded'
);
