import { createSelector } from '@ngrx/store';
import { PageState } from './page.state';
import { selectPageState } from '@app/core/core.state';

export const selectPageShowFooter = createSelector(
  selectPageState,
  (state: PageState) => state.showFooter
);
