import { selectPageState } from '@app/core';
import { createSelector } from '@ngrx/store';
import { PageState } from './page.state';

export const selectPageShowFooter = createSelector(
  selectPageState,
  (state: PageState) => state.showFooter
);
