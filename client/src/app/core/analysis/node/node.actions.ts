import {NodeChangesPage} from '@api/common/node/node-changes-page';
import {NodeDetailsPage} from '@api/common/node/node-details-page';
import {NodeMapPage} from '@api/common/node/node-map-page';
import {ApiResponse} from '@api/custom/api-response';
import {createAction} from '@ngrx/store';
import {props} from '@ngrx/store';

export const actionNodeLink = createAction(
  '[Node] Link',
  props<{ nodeId: string; nodeName: string }>()
);

export const actionNodeDetailsLoaded = createAction(
  '[Node] Details loaded',
  props<{ response: ApiResponse<NodeDetailsPage> }>()
);

export const actionNodeMapLoaded = createAction(
  '[Node] Map loaded',
  props<{ response: ApiResponse<NodeMapPage> }>()
);

export const actionNodeChangesLoaded = createAction(
  '[Node] Changes loaded',
  props<{ response: ApiResponse<NodeChangesPage> }>()
);
