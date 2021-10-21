import { createFeatureSelector } from '@ngrx/store';
import { createSelector } from '@ngrx/store';
import { RouteRootState } from './route.state';
import { routeFeatureKey } from './route.state';
import { RouteState } from './route.state';

export const selectRouteState = createFeatureSelector<
  RouteRootState,
  RouteState
>(routeFeatureKey);

export const selectRouteDetailsPage = createSelector(
  selectRouteState,
  (state: RouteState) => state.detailsPage
);

export const selectRouteMapPage = createSelector(
  selectRouteState,
  (state: RouteState) => state.mapPage
);

export const selectNodeChangesPage = createSelector(
  selectRouteState,
  (state: RouteState) => state.changesPage
);

export const selectRouteId = createSelector(
  selectRouteState,
  (state: RouteState) => state.routeId
);

export const selectRouteName = createSelector(
  selectRouteState,
  (state: RouteState) => state.routeName
);

export const selectRouteNetworkType = createSelector(
  selectRouteState,
  (state: RouteState) => state.networkType
);

export const selectRouteChangeCount = createSelector(
  selectRouteState,
  (state: RouteState) => state.changeCount
);
