import { SurveyDateValues } from './survey-date-values';

export const initialSharedState: SharedState = {
  httpError: null,
  surveyDateInfo: null,
};

export interface SharedState {
  httpError: string | null;
  surveyDateInfo: SurveyDateValues | null;
}
