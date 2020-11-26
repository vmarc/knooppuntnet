import {createAction} from '@ngrx/store';
import {props} from '@ngrx/store';
import {ApiResponse} from '../../../kpn/api/custom/api-response';
import {NodeDetailsPage} from '../../../kpn/api/common/node/node-details-page';
import {NodeChangesPage} from '../../../kpn/api/common/node/node-changes-page';
import {NodeMapPage} from '../../../kpn/api/common/node/node-map-page';

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
