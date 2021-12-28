import { ChangesPage } from '@api/common/changes-page';
import { ApiResponse } from '@api/custom/api-response';
import { props } from '@ngrx/store';
import { createAction } from '@ngrx/store';

export interface ChangeOption {
  year: number;
  month: number;
  day: number;
  impact: boolean;
}

export const actionChangesPageInit = createAction('[ChangesPage] Init');

export const actionChangesPageIndex = createAction(
  '[ChangesPage] Page index',
  props<{ pageIndex: number }>()
);

export const actionChangesPageLoaded = createAction(
  '[ChangesPage] Loaded',
  props<{ response: ApiResponse<ChangesPage> }>()
);

export const actionChangesFilterOption = createAction(
  '[ChangesPage] FilterOption',
  props<{ option: ChangeOption }>()
);
