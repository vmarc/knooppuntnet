import {createReducer, on} from '@ngrx/store';
import {decrement, increment, reset, set} from './counter.actions';
import {initialState} from './counter.state';
import {CounterState} from './counter.state';

export const counterReducer = createReducer(
  initialState,
  on(increment,
    (state: CounterState) => ({
      ...state,
      counter: state.counter + 1
    })
  ),
  on(decrement,
    (state: CounterState) => ({
      ...state, counter: state.counter - 1
    })
  ),
  on(reset,
    (state: CounterState) => ({
      ...state, counter: 0
    })
  ),
  on(set, (state: CounterState, {value}) => ({
      ...state,
      counter: value
    })
  )
);
