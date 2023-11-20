import { ChangesParameters } from '@api/common/changes/filter';
import { NodeChangesPage } from '@api/common/node';
import { NodeDetailsPage } from '@api/common/node';
import { NodeMapPage } from '@api/common/node';
import { ApiResponse } from '@api/custom';
import { MapPosition } from '@app/ol/domain';
import { ChangeOption } from '@app/kpn/common';
import { createAction } from '@ngrx/store';
import { props } from '@ngrx/store';

export const actionNodeDetailsPageInit = createAction('[NodeDetailsPage] Init');

export const actionNodeDetailsPageLoad = createAction(
  '[NodeDetailsPage] Load',
  props<{ nodeId: string; nodeName: string | undefined }>()
);

export const actionNodeDetailsPageLoaded = createAction(
  '[NodeDetailsPage] Loaded',
  props<ApiResponse<NodeDetailsPage>>()
);

export const actionNodeDetailsPageDestroy = createAction(
  '[NodeDetailsPage] Destroy'
);

export const actionNodeMapPageInit = createAction('[NodeMapPage] Init');

export const actionNodeMapPageLoad = createAction(
  '[NodeMapPage] Load',
  props<{ nodeId: string; mapPositionFromUrl: MapPosition }>()
);

export const actionNodeMapViewInit = createAction('[NodeMapPage] ViewInit');

export const actionNodeMapPageLoaded = createAction(
  '[NodeMapPage] Loaded',
  props<{
    response: ApiResponse<NodeMapPage>;
    mapPositionFromUrl: MapPosition;
  }>()
);

export const actionNodeMapPageDestroy = createAction('[NodeMapPage] Destroy');

export const actionNodeChangesPageInit = createAction('[NodeChangesPage] Init');

export const actionNodeChangesPageLoad = createAction(
  '[NodeChangesPage] Load',
  props<{ nodeId: string; changesParameters: ChangesParameters }>()
);

export const actionNodeChangesPageLoaded = createAction(
  '[NodeChangesPage] Loaded',
  props<ApiResponse<NodeChangesPage>>()
);

export const actionNodeChangesPageDestroy = createAction(
  '[NodeChangesPage] Destroy'
);

export const actionNodeChangesPageImpact = createAction(
  '[NodeChangesPage] Impact',
  props<{ impact: boolean }>()
);

export const actionNodeChangesPageSize = createAction(
  '[NodeChangesPage] Page size',
  props<{ pageSize: number }>()
);

export const actionNodeChangesPageIndex = createAction(
  '[NodeChangesPage] Page index',
  props<{ pageIndex: number }>()
);

export const actionNodeChangesFilterOption = createAction(
  '[NodeChangesPage] Filter option',
  props<{ option: ChangeOption }>()
);
