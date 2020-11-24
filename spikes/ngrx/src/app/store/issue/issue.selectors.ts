import {createSelector} from '@ngrx/store';
import {selectIssueState} from '../app.state';
import {IssueState} from './issue.state';

export const selectIssues = createSelector(
  selectIssueState,
  (state: IssueState) => Object.values(state.entities)
);

export const selectIssueActive = createSelector(
  selectIssueState,
  (state: IssueState) => state.entities.id1
);
