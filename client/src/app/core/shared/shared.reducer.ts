import {createReducer} from '@ngrx/store';
import {SharedState} from './shared.state';

const initialSharedState: SharedState = {};

export const sharedReducer = createReducer(
  initialSharedState
);
