import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { RouteChangesPage } from '@api/common/route/route-changes-page';
import { RouteDetailsPage } from '@api/common/route/route-details-page';
import { RouteMapPage } from '@api/common/route/route-map-page';
import { ApiResponse } from '@api/custom/api-response';
import { NetworkType } from '@api/custom/network-type';
import { createAction } from '@ngrx/store';
import { props } from '@ngrx/store';
import { MapPosition } from '@app/components/ol/domain/map-position';
import { ChangeOption } from '../../changes/store/changes.actions';

export const actionRouteLink = createAction(
  '[Route] Link',
  props<{ routeId: string; routeName: string; networkType: NetworkType }>()
);

export const actionRouteDetailsPageInit = createAction(
  '[RouteDetailsPage] Init'
);

export const actionRouteDetailsPageLoad = createAction(
  '[RouteDetailsPage] Load',
  props<{ routeId: string }>()
);

export const actionRouteDetailsPageLoaded = createAction(
  '[RouteDetailsPage] Loaded',
  props<ApiResponse<RouteDetailsPage>>()
);

export const actionRouteMapPageInit = createAction('[RouteMapPage] Init');

export const actionRouteMapPageLoad = createAction(
  '[RouteMapPage] Load',
  props<{ routeId: string; mapPositionFromUrl: MapPosition }>()
);

export const actionRouteMapPageLoaded = createAction(
  '[RouteMapPage] Loaded',
  props<{
    response: ApiResponse<RouteMapPage>;
    mapPositionFromUrl: MapPosition;
  }>()
);

export const actionRouteChangesPageInit = createAction(
  '[RouteChangesPage] Init'
);

export const actionRouteChangesPageLoad = createAction(
  '[RouteChangesPage] Load',
  props<{ routeId: string; changesParameters: ChangesParameters }>()
);

export const actionRouteChangesPageLoaded = createAction(
  '[RouteChangesPage] Loaded',
  props<ApiResponse<RouteChangesPage>>()
);

export const actionRouteChangesPageImpact = createAction(
  '[RouteChangesPage] Impact',
  props<{ impact: boolean }>()
);

export const actionRouteChangesPageSize = createAction(
  '[RouteChangesPage] Page size',
  props<{ pageSize: number }>()
);

export const actionRouteChangesPageIndex = createAction(
  '[RouteChangesPage] Page index',
  props<{ pageIndex: number }>()
);

export const actionRouteChangesFilterOption = createAction(
  '[RouteChangesPage] Filter option',
  props<{ option: ChangeOption }>()
);
