import {createSelector} from '@ngrx/store';
import {selectCounterState} from '../app.state';
import {CounterState} from './counter.state';

export const selectCounterValue = createSelector(
  selectCounterState,
  (state: CounterState) => state.counter
);
