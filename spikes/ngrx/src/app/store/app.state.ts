import {ActionReducerMap} from '@ngrx/store';
import {createFeatureSelector} from '@ngrx/store';
import {counterReducer} from './counter/counter.reducer';
import {CounterState} from './counter/counter.state';
import {issueReducer} from './issue/issue.reducer';
import {IssueState} from './issue/issue.state';
import {userReducer} from './user/user.reducer';
import {UserState} from './user/user.state';

export const reducers: ActionReducerMap<AppState> = {
  counter: counterReducer,
  issue: issueReducer,
  user: userReducer
};

export const selectCounterState = createFeatureSelector<AppState, CounterState>('counter');

export const selectIssueState = createFeatureSelector<AppState, IssueState>('issue');

export const selectUserState = createFeatureSelector<AppState, UserState>('user');

//export const selectSettingsState = createFeatureSelector<AppState, SettingsState>('settings');

export interface AppState {
  counter: CounterState;
  issue: IssueState;
  user: UserState;
//  settings: SettingsState;
}
