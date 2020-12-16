import {props} from '@ngrx/store';
import {createAction} from '@ngrx/store';
import {NetworkChangesPage} from '@api/common/network/network-changes-page';
import {NetworkDetailsPage} from '@api/common/network/network-details-page';
import {NetworkFactsPage} from '@api/common/network/network-facts-page';
import {NetworkMapPage} from '@api/common/network/network-map-page';
import {NetworkNodesPage} from '@api/common/network/network-nodes-page';
import {NetworkRoutesPage} from '@api/common/network/network-routes-page';
import {ApiResponse} from '@api/custom/api-response';

export const actionNetworkLink = createAction(
  '[Network] Link',
  props<{ networkId: string; networkName: string }>()
);

export const actionNetworkDetailsLoaded = createAction(
  '[Network] Details loaded',
  props<{ response: ApiResponse<NetworkDetailsPage> }>()
);

export const actionNetworkNodesLoaded = createAction(
  '[Network] Nodes loaded',
  props<{ response: ApiResponse<NetworkNodesPage> }>()
);

export const actionNetworkRoutesLoaded = createAction(
  '[Network] Routes loaded',
  props<{ response: ApiResponse<NetworkRoutesPage> }>()
);

export const actionNetworkFactsLoaded = createAction(
  '[Network] Facts loaded',
  props<{ response: ApiResponse<NetworkFactsPage> }>()
);

export const actionNetworkMapLoaded = createAction(
  '[Network] Map loaded',
  props<{ response: ApiResponse<NetworkMapPage> }>()
);

export const actionNetworkChangesLoaded = createAction(
  '[Network] Changes loaded',
  props<{ response: ApiResponse<NetworkChangesPage> }>()
);
