import { routerNavigationAction } from '@ngrx/router-store';
import { on } from '@ngrx/store';
import { createReducer } from '@ngrx/store';
import { RoutingUtil } from '../../base/routing-util';
import { actionSharedHttpError } from './shared.actions';
import { initialSharedState } from './shared.state';

export const sharedReducer = createReducer(
  initialSharedState,
  on(routerNavigationAction, (state, action) => {
    const util = new RoutingUtil(action);
    return {
      ...state,
      httpError: null,
    };
  }),
  on(actionSharedHttpError, (state, { httpError }) => ({
    ...state,
    httpError,
  }))
);
