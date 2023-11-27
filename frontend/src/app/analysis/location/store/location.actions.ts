import { LocationChangesPage } from '@api/common/location';
import { LocationEditPage } from '@api/common/location';
import { LocationFactsPage } from '@api/common/location';
import { LocationMapPage } from '@api/common/location';
import { LocationNodesPage } from '@api/common/location';
import { LocationRoutesPage } from '@api/common/location';
import { ApiResponse } from '@api/custom';
import { LocationNodesType } from '@api/custom';
import { LocationRoutesType } from '@api/custom';
import { MapPosition } from '@app/ol/domain';
import { AnalysisStrategy } from '@app/core';
import { props } from '@ngrx/store';
import { createAction } from '@ngrx/store';

export const actionLocationSelectionPageInit = createAction('[LocationSelectionPage] Init');

export const actionLocationSelectionPageDestroy = createAction('[LocationSelectionPage] Destroy');

export const actionLocationSelectionPageStrategy = createAction(
  '[LocationSelectionPage] Strategy',
  props<{ strategy: AnalysisStrategy }>()
);

export const actionLocationNodesPageInit = createAction('[LocationNodesPage] Init');

export const actionLocationNodesPageDestroy = createAction('[LocationNodesPage] Destroy');

export const actionLocationNodesType = createAction(
  '[LocationNodesPage] Type',
  props<{ locationNodesType: LocationNodesType }>()
);

export const actionLocationNodesPageSize = createAction(
  '[LocationNodesPage] Page size',
  props<{ pageSize: number }>()
);

export const actionLocationNodesPageIndex = createAction(
  '[LocationNodesPage] Page index',
  props<{ pageIndex: number }>()
);

export const actionLocationNodesPageLoaded = createAction(
  '[LocationNodesPage] Loaded',
  props<ApiResponse<LocationNodesPage>>()
);

export const actionLocationRoutesPageInit = createAction('[LocationRoutesPage] Init');

export const actionLocationRoutesPageDestroy = createAction('[LocationRoutesPage] Destroy');

export const actionLocationRoutesType = createAction(
  '[LocationRoutesPage] Type',
  props<{ locationRoutesType: LocationRoutesType }>()
);

export const actionLocationRoutesPageSize = createAction(
  '[LocationRoutesPage] Page size',
  props<{ pageSize: number }>()
);

export const actionLocationRoutesPageIndex = createAction(
  '[LocationRoutesPage] Page index',
  props<{ pageIndex: number }>()
);

export const actionLocationRoutesPageLoaded = createAction(
  '[LocationRoutesPage] Loaded',
  props<ApiResponse<LocationRoutesPage>>()
);

export const actionLocationFactsPageInit = createAction('[LocationFactsPage] Init');

export const actionLocationFactsPageDestroy = createAction('[LocationFactsPage] Destroy');

export const actionLocationFactsPageLoaded = createAction(
  '[LocationFactsPage] Loaded',
  props<ApiResponse<LocationFactsPage>>()
);

export const actionLocationMapPageInit = createAction('[LocationMapPage] Init');

export const actionLocationMapViewInit = createAction('[LocationMapPage] ViewInit');

export const actionLocationMapPageDestroy = createAction('[LocationMapPage] Destroy');

export const actionLocationMapPageLoaded = createAction(
  '[LocationMapPage] Loaded',
  props<{
    response: ApiResponse<LocationMapPage>;
  }>()
);

export const actionLocationMapPosition = createAction(
  '[LocationMapPage] Position',
  props<{ mapPosition: MapPosition }>()
);

export const actionLocationChangesPageInit = createAction('[LocationChangesPage] Init');

export const actionLocationChangesPageDestroy = createAction('[LocationChangesPage] Destroy');

export const actionLocationChangesPageLoaded = createAction(
  '[LocationChangesPage] Loaded',
  props<ApiResponse<LocationChangesPage>>()
);

export const actionLocationEditPageInit = createAction('[LocationEditPage] Init');

export const actionLocationEditPageDestroy = createAction('[LocationEditPage] Destroy');

export const actionLocationEditPageLoaded = createAction(
  '[LocationEditPage] Loaded',
  props<ApiResponse<LocationEditPage>>()
);
