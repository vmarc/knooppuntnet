import { NodeChangesPage } from '@api/common/node/node-changes-page';
import { NodeDetailsPage } from '@api/common/node/node-details-page';
import { NodeMapPage } from '@api/common/node/node-map-page';
import { ApiResponse } from '@api/custom/api-response';
import { createAction } from '@ngrx/store';
import { props } from '@ngrx/store';

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

export const actionNodeChangesPageLoaded = createAction(
  '[NodeChangesPage] Loaded',
  props<{ response: ApiResponse<NodeChangesPage> }>()
);
