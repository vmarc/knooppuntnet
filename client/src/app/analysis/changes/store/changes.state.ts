import { ChangesPage } from '@api/common/changes-page';
import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { ApiResponse } from '@api/custom/api-response';

export const initialState: ChangesState = {
  changesParameters: null,
  changesPage: null,
};

export interface ChangesState {
  changesParameters: ChangesParameters;
  changesPage: ApiResponse<ChangesPage>;
}

export const changesFeatureKey = 'changes';
