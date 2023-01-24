import { routerNavigationAction } from '@ngrx/router-store';
import { on } from '@ngrx/store';
import { createReducer } from '@ngrx/store';
import { actionSharedHttpError } from './shared.actions';
import { initialSharedState } from './shared.state';

export const sharedReducer = createReducer<SharedState>(
  initialSharedState,
  on(routerNavigationAction, (state, action): SharedState => {
    return {
      ...state,
      httpError: null,
    };
  }),
  on(
    actionSharedHttpError,
    (state, { httpError }): SharedState => ({
      ...state,
      httpError,
    })
  )
);
