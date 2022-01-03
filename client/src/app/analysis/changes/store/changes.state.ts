import { ChangesPage } from '@api/common/changes-page';
import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { ApiResponse } from '@api/custom/api-response';
import { AnalysisStrategy } from '../../../core/preferences/preferences.state';

export const initialState: ChangesState = {
  strategy: AnalysisStrategy.location,
  changesParameters: null,
  changesPage: null,
};

export interface ChangesState {
  strategy: AnalysisStrategy;
  changesParameters: ChangesParameters;
  changesPage: ApiResponse<ChangesPage>;
}

export const changesFeatureKey = 'changes';
