import { on } from '@ngrx/store';
import { createReducer } from '@ngrx/store';
import { actionFrisoMode } from './friso.actions';
import { actionFrisoMapInitialized } from './friso.actions';
import { initialState } from './friso.state';
import { FrisoState } from './friso.state';

export const frisoReducer = createReducer<FrisoState>(
  initialState,
  on(actionFrisoMapInitialized, (state, { mode }): FrisoState => {
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
