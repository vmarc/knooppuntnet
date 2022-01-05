import { LocationChangesPage } from '@api/common/location/location-changes-page';
import { LocationEditPage } from '@api/common/location/location-edit-page';
import { LocationFactsPage } from '@api/common/location/location-facts-page';
import { LocationMapPage } from '@api/common/location/location-map-page';
import { LocationNodesPage } from '@api/common/location/location-nodes-page';
import { LocationRoutesPage } from '@api/common/location/location-routes-page';
import { ApiResponse } from '@api/custom/api-response';
import { LocationNodesType } from '@api/custom/location-nodes-type';
import { LocationRoutesType } from '@api/custom/location-routes-type';
import { props } from '@ngrx/store';
import { createAction } from '@ngrx/store';
import { AnalysisStrategy } from '../../../core/preferences/preferences.state';

export const actionLocationSelectionPageInit = createAction(
  '[LocationSelectionPage] Init'
);

export const actionLocationSelectionPageStrategy = createAction(
  '[LocationSelectionPage] Strategy',
  props<{ strategy: AnalysisStrategy }>()
);

export const actionLocationNodesPageInit = createAction(
  '[LocationNodesPage] Init'
);

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
  props<{ response: ApiResponse<LocationNodesPage> }>()
);

export const actionLocationRoutesPageInit = createAction(
  '[LocationRoutesPage] Init'
);

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
  props<{ response: ApiResponse<LocationRoutesPage> }>()
);

export const actionLocationFactsPageInit = createAction(
  '[LocationFactsPage] Init'
);

export const actionLocationFactsPageLoaded = createAction(
  '[LocationFactsPage] Loaded',
  props<{ response: ApiResponse<LocationFactsPage> }>()
);

export const actionLocationMapPageInit = createAction('[LocationMapPage] Init');

export const actionLocationMapPageLoaded = createAction(
  '[LocationMapPage] Loaded',
  props<{ response: ApiResponse<LocationMapPage> }>()
);

export const actionLocationChangesPageInit = createAction(
  '[LocationChangesPage] Init'
);

export const actionLocationChangesPageLoaded = createAction(
  '[LocationChangesPage] Loaded',
  props<{ response: ApiResponse<LocationChangesPage> }>()
);

export const actionLocationEditPageInit = createAction(
  '[LocationEditPage] Init'
);

export const actionLocationEditPageLoaded = createAction(
  '[LocationEditPage] Loaded',
  props<{ response: ApiResponse<LocationEditPage> }>()
);
