import {createSelector} from '@ngrx/store';
import {selectUserState} from '../app.state';
import {UserState} from './user.state';

export const selectUsers = createSelector(
  selectUserState,
  (state: UserState) => Object.values(state.users)
);
