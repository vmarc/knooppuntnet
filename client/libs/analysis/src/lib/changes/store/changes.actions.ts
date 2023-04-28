import { ChangesPage } from '@api/common';
import { ChangesParameters } from '@api/common/changes/filter';
import { ApiResponse } from '@api/custom';
import { AnalysisStrategy } from '@app/core';
import { ChangeOption } from '@app/kpn/common';
import { props } from '@ngrx/store';
import { createAction } from '@ngrx/store';

export const actionChangesPageInit = createAction('[ChangesPage] Init');

export const actionChangesPageLoad = createAction(
  '[ChangesPage] Load',
  props<{
    strategy: AnalysisStrategy;
    changesParameters: ChangesParameters;
  }>()
);

export const actionChangesPageLoaded = createAction(
  '[ChangesPage] Loaded',
  props<ApiResponse<ChangesPage>>()
);

export const actionChangesImpact = createAction(
  '[ChangesPage] Impact',
  props<{ impact: boolean }>()
);

export const actionChangesPageSize = createAction(
  '[ChangesPage] Page size',
  props<{ pageSize: number }>()
);

export const actionChangesPageIndex = createAction(
  '[ChangesPage] Page index',
  props<{ pageIndex: number }>()
);

export const actionChangesAnalysisStrategy = createAction(
  '[ChangesPage] Analysis strategy',
  props<{ strategy: AnalysisStrategy }>()
);

export const actionChangesFilterOption = createAction(
  '[ChangesPage] FilterOption',
  props<{ option: ChangeOption }>()
);
