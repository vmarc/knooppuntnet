import { createFeatureSelector } from '@ngrx/store';
import { createSelector } from '@ngrx/store';
import { PageState } from './page.state';

export const selectPageState = createFeatureSelector<PageState>('page');

export const selectPageShowFooter = createSelector(
  selectPageState,
  (state: PageState) => true //state.showFooter
);
