import {createReducer} from '@ngrx/store';
import {on} from '@ngrx/store';
import {initialState} from './issue.state';
import * as IssueActions from './issue.actions';

export const issueReducer = createReducer(
  initialState,
  on(IssueActions.submit, (state, {issue}) => ({
      ...state,
      issues: {
        ...state.issues,
        [issue.id]: {
          ...issue,
          resolved: false
        }
      }
    })
  )
);
