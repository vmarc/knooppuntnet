import { createAction } from '@ngrx/store';
import { props } from '@ngrx/store';
import { AnalysisStrategy } from './preferences.state';

export const actionPreferencesAnalysisStrategy = createAction(
  '[Preferences] AnalysisStrategy',
  props<{ strategy: AnalysisStrategy }>()
);
