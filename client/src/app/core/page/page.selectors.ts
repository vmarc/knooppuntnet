import { createSelector } from '@ngrx/store';
import { selectPageState } from '../core.state';
import { PageState } from './page.state';

export const selectPageShowFooter = createSelector(
  selectPageState,
  (state: PageState) => state.showFooter
);
