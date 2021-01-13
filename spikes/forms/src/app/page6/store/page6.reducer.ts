import {on} from '@ngrx/store';
import {createReducer} from '@ngrx/store';
import {actionPage6a} from './page6.actions';
import {initialState} from './page6.state';

export const page6Reducer = createReducer(
  initialState,
  on(
    actionPage6a,
    (state, {admin}) => ({
      ...state,
      admin,
    })
  )
);
