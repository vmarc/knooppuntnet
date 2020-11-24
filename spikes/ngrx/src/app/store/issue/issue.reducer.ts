import {createReducer} from '@ngrx/store';
import {initialState} from './issue.state';

export const issueReducer = createReducer(
  initialState
);
