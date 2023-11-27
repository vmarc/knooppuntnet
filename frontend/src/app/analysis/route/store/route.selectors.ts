import { ChangesParameters } from '@api/common/changes/filter';
import { createFeatureSelector } from '@ngrx/store';
import { createSelector } from '@ngrx/store';
import { routeFeatureKey } from './route.state';
import { RouteState } from './route.state';

export const selectRouteState = createFeatureSelector<RouteState>(routeFeatureKey);

export const selectRouteDetailsPage = createSelector(
  selectRouteState,
  (state: RouteState) => state.detailsPage
);

export const selectRouteMapPage = createSelector(
  selectRouteState,
  (state: RouteState) => state.mapPage
);

export const selectRouteChangesPage = createSelector(
  selectRouteState,
  (state: RouteState) => state.changesPage
);

export const selectRouteId = createSelector(selectRouteState, (state: RouteState) => state.routeId);

export const selectRouteName = createSelector(
  selectRouteState,
  (state: RouteState) => state.routeName
);

export const selectRouteNetworkType = createSelector(
  selectRouteState,
  (state: RouteState) => state.networkType
);

export const selectRouteMapPositionFromUrl = createSelector(
  selectRouteState,
  (state: RouteState) => state.mapPositionFromUrl
);

export const selectRouteChangeCount = createSelector(
  selectRouteState,
  (state: RouteState) => state.changeCount
);

export const selectRouteChangesParameters = createSelector(
  selectRouteState,
  (state: RouteState) => state.changesParameters
);

export const selectRouteChangesPageImpact = createSelector(
  selectRouteChangesParameters,
  (changesParameters: ChangesParameters) => changesParameters.impact
);

export const selectRouteChangesPageSize = createSelector(
  selectRouteChangesParameters,
  (changesParameters: ChangesParameters) => changesParameters.pageSize
);

export const selectRouteChangesPageIndex = createSelector(
  selectRouteChangesParameters,
  (changesParameters: ChangesParameters) => changesParameters.pageIndex
);

export const selectRouteChangesFilterOptions = createSelector(
  selectRouteState,
  (state: RouteState) => state.changesPage?.result?.filterOptions
);
