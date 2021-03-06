import { NetworkChangesPage } from '@api/common/network/network-changes-page';
import { NetworkDetailsPage } from '@api/common/network/network-details-page';
import { NetworkFactsPage } from '@api/common/network/network-facts-page';
import { NetworkMapPage } from '@api/common/network/network-map-page';
import { NetworkNodesPage } from '@api/common/network/network-nodes-page';
import { NetworkRoutesPage } from '@api/common/network/network-routes-page';
import { ApiResponse } from '@api/custom/api-response';
import { props } from '@ngrx/store';
import { createAction } from '@ngrx/store';

export const actionNetworkLink = createAction(
  '[Network] Link',
  props<{ networkId: string; networkName: string }>()
);

export const actionNetworkDetailsPageInit = createAction(
  '[NetworkDetailsPage] Init'
);

export const actionNetworkDetailsPageLoaded = createAction(
  '[NetworkDetailsPage] Loaded',
  props<{ response: ApiResponse<NetworkDetailsPage> }>()
);

export const actionNetworkNodesPageInit = createAction(
  '[NetworkNodesPage] Init'
);

export const actionNetworkNodesPageLoaded = createAction(
  '[NetworkNodesPage] Loaded',
  props<{ response: ApiResponse<NetworkNodesPage> }>()
);

export const actionNetworkRoutesPageInit = createAction(
  '[NetworkRoutesPage] Init'
);

export const actionNetworkRoutesPageLoaded = createAction(
  '[NetworkRoutesPage] Loaded',
  props<{ response: ApiResponse<NetworkRoutesPage> }>()
);

export const actionNetworkFactsPageInit = createAction(
  '[NetworkFactsPage] Init'
);

export const actionNetworkFactsPageLoaded = createAction(
  '[NetworkFactsPage] Loaded',
  props<{ response: ApiResponse<NetworkFactsPage> }>()
);

export const actionNetworkMapPageInit = createAction('[NetworkMapPage] Init');

export const actionNetworkMapPageLoaded = createAction(
  '[NetworkMapPage] Loaded',
  props<{ response: ApiResponse<NetworkMapPage> }>()
);

export const actionNetworkChangesPageInit = createAction(
  '[NetworkChangesPage] Init'
);

export const actionNetworkChangesPageLoaded = createAction(
  '[NetworkChangesPage] Loaded',
  props<{ response: ApiResponse<NetworkChangesPage> }>()
);
