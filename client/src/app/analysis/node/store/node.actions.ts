import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { NodeChangesPage } from '@api/common/node/node-changes-page';
import { NodeDetailsPage } from '@api/common/node/node-details-page';
import { NodeMapPage } from '@api/common/node/node-map-page';
import { ApiResponse } from '@api/custom/api-response';
import { createAction } from '@ngrx/store';
import { props } from '@ngrx/store';
import { ChangeOption } from '../../changes/store/changes.actions';

export const actionNodeId = createAction(
  '[Node] Id',
  props<{ nodeId: string }>()
);

export const actionNodeLink = createAction(
  '[Node] Link',
  props<{ nodeId: string; nodeName: string }>()
);

export const actionNodeDetailsPageInit = createAction('[NodeDetailsPage] Init');

export const actionNodeDetailsPageLoaded = createAction(
  '[NodeDetailsPage] Loaded',
  props<{ response: ApiResponse<NodeDetailsPage> }>()
);

export const actionNodeMapPageInit = createAction('[NodeMapPage] Init');

export const actionNodeMapPageLoaded = createAction(
  '[NodeMapPage] Loaded',
  props<{ response: ApiResponse<NodeMapPage> }>()
);

export const actionNodeChangesPageInit = createAction('[NodeChangesPage] Init');

export const actionNodeChangesPageLoad = createAction(
  '[NodeChangesPage] Load',
  props<{ nodeId: string; changesParameters: ChangesParameters }>()
);

export const actionNodeChangesPageLoaded = createAction(
  '[NodeChangesPage] Loaded',
  props<{ response: ApiResponse<NodeChangesPage> }>()
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
