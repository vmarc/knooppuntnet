import { ChangesPage } from '@api/common';
import { ChangesParameters } from '@api/common/changes/filter';
import { ApiResponse } from '@api/custom';

export const initialState: ChangesState = {
  changesParameters: null,
  changesPage: null,
};

export interface ChangesState {
  changesParameters: ChangesParameters;
  changesPage: ApiResponse<ChangesPage>;
}

export const changesFeatureKey = 'changes';
