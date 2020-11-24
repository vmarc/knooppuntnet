import {IssueState} from './issue/issue.state';
import {ActionReducerMap} from '@ngrx/store';
import {createFeatureSelector} from '@ngrx/store';
import {CounterState} from './counter/counter.state';
import {counterReducer} from './counter/counter.reducer';
import {issueReducer} from './issue/issue.reducer';

export const reducers: ActionReducerMap<AppState> = {
  counter: counterReducer,
  issue: issueReducer
};

export const selectCounterState = createFeatureSelector<AppState, CounterState>('counter');

export const selectIssueState = createFeatureSelector<AppState, IssueState>('issue');

//export const selectSettingsState = createFeatureSelector<AppState, SettingsState>('settings');

export interface AppState {
  counter: CounterState;
  issue: IssueState;
//  settings: SettingsState;
}
