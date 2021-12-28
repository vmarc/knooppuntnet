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

export const selectChangesAnalysisMode = createSelector(
  selectChangesState,
  (state: ChangesState) => state.analysisMode
);

export const selectChangesPageIndex = createSelector(
  selectChangesState,
  (state: ChangesState) => state.pageIndex
);
