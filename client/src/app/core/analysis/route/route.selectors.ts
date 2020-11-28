import {createSelector} from '@ngrx/store';
import {selectRouteState} from '../../core.state';
import {RouteState} from './route.state';

export const selectRouteDetails = createSelector(
  selectRouteState,
  (state: RouteState) => state.details
);

export const selectNodeMap = createSelector(
  selectRouteState,
  (state: RouteState) => state.map
);

export const selectNodeChanges = createSelector(
  selectRouteState,
  (state: RouteState) => state.changes
);
