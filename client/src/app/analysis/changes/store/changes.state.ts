import { ChangesPage } from '@api/common/changes-page';
import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { ApiResponse } from '@api/custom/api-response';
import { AnalysisMode } from '../../../core/preferences/preferences.state';

export const initialState: ChangesState = {
  analysisMode: AnalysisMode.location,
  changesParameters: null,
  changesPage: null,
};

export interface ChangesState {
  analysisMode: AnalysisMode;
  changesParameters: ChangesParameters;
  changesPage: ApiResponse<ChangesPage>;
}

export const changesFeatureKey = 'changes';
