import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { createSelector } from '@ngrx/store';
import { createFeatureSelector } from '@ngrx/store';
import { changesFeatureKey } from './changes.state';
import { ChangesState } from './changes.state';

export const selectChangesState =
  createFeatureSelector<ChangesState>(changesFeatureKey);

export const selectChangesPage = createSelector(
  selectChangesState,
  (state: ChangesState) => state.changesPage
);

export const selectChangesFilterOptions = createSelector(
  selectChangesState,
  (state: ChangesState) => state.changesPage?.result?.filterOptions
);

export const selectChangesParameters = createSelector(
  selectChangesState,
  (state: ChangesState) => state.changesParameters
);

export const selectChangesImpact = createSelector(
  selectChangesParameters,
  (changesParameters: ChangesParameters) => changesParameters.impact
);

export const selectChangesPageSize = createSelector(
  selectChangesParameters,
  (changesParameters: ChangesParameters) => changesParameters.pageSize
);

export const selectChangesPageIndex = createSelector(
  selectChangesParameters,
  (changesParameters: ChangesParameters) => changesParameters.pageIndex
);
