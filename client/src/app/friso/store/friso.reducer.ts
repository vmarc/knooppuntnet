import { on } from '@ngrx/store';
import { createReducer } from '@ngrx/store';
import { initialState } from './friso.state';
import { FrisoState } from './friso.state';
import { actionFrisoMode } from './friso.actions';
import { actionFrisoPageInit } from './friso.actions';
import { actionFrisoPageLoad } from './friso.actions';

export const frisoReducer = createReducer<FrisoState>(
  initialState,
  on(actionFrisoPageInit, (state): FrisoState => {
    return {
      ...state,
      mode: 'rename',
    };
  }),
  on(actionFrisoPageLoad, (state, { mode }): FrisoState => {
    return {
      ...state,
      mode,
    };
  }),
  on(
    actionFrisoMode,
    (state, { mode }): FrisoState => ({
      ...state,
      mode,
    })
  )
);
