import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { NodeChangesPage } from '@api/common/node/node-changes-page';
import { NodeDetailsPage } from '@api/common/node/node-details-page';
import { NodeMapPage } from '@api/common/node/node-map-page';
import { ApiResponse } from '@api/custom/api-response';
import { createAction } from '@ngrx/store';
import { props } from '@ngrx/store';
import { MapPosition } from '@app/components/ol/domain/map-position';
import { ChangeOption } from '../../changes/store/changes.actions';

export const actionNodeLink = createAction(
  '[Node] Link',
  props<{ nodeId: string; nodeName: string }>()
);

export const actionNodeDetailsPageInit = createAction('[NodeDetailsPage] Init');

export const actionNodeDetailsPageLoad = createAction(
  '[NodeDetailsPage] Load',
  props<{ nodeId: string }>()
);

export const actionNodeDetailsPageLoaded = createAction(
  '[NodeDetailsPage] Loaded',
  props<ApiResponse<NodeDetailsPage>>()
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

export const actionNodeChangesPageInit = createAction('[NodeChangesPage] Init');

export const actionNodeChangesPageLoad = createAction(
  '[NodeChangesPage] Load',
  props<{ nodeId: string; changesParameters: ChangesParameters }>()
);

export const actionNodeChangesPageLoaded = createAction(
  '[NodeChangesPage] Loaded',
  props<ApiResponse<NodeChangesPage>>()
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
