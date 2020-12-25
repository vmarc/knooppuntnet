import {createSelector} from '@ngrx/store';
import {selectSharedState} from '../core.state';
import {SharedState} from './shared.state';

export const selectSharedHttpError = createSelector(
  selectSharedState,
  (state: SharedState) => state.httpError
);
