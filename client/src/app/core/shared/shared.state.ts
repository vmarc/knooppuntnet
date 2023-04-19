import { SurveyDateValues } from '@app/components/ol/services';

export const initialSharedState: SharedState = {
  httpError: null,
  surveyDateInfo: null,
};

export interface SharedState {
  httpError: string | null;
  surveyDateInfo: SurveyDateValues | null;
}
