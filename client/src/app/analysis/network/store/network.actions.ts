import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { NetworkChangesPage } from '@api/common/network/network-changes-page';
import { NetworkDetailsPage } from '@api/common/network/network-details-page';
import { NetworkFactsPage } from '@api/common/network/network-facts-page';
import { NetworkMapPage } from '@api/common/network/network-map-page';
import { NetworkNodesPage } from '@api/common/network/network-nodes-page';
import { NetworkRoutesPage } from '@api/common/network/network-routes-page';
import { ApiResponse } from '@api/custom/api-response';
import { NetworkType } from '@api/custom/network-type';
import { props } from '@ngrx/store';
import { createAction } from '@ngrx/store';
import { NetworkMapPosition } from '../../../components/ol/domain/network-map-position';
import { ChangeOption } from '../../changes/store/changes.actions';

export const actionNetworkLink = createAction(
  '[Network] Link',
  props<{ networkId: number; networkName: string; networkType: NetworkType }>()
);

export const actionNetworkDetailsPageInit = createAction(
  '[NetworkDetailsPage] Init'
);

export const actionNetworkDetailsPageLoad = createAction(
  '[NetworkDetailsPage] Load',
  props<{ networkId: number }>()
);

export const actionNetworkDetailsPageLoaded = createAction(
  '[NetworkDetailsPage] Loaded',
  props<ApiResponse<NetworkDetailsPage>>()
);

export const actionNetworkNodesPageInit = createAction(
  '[NetworkNodesPage] Init'
);

export const actionNetworkNodesPageLoad = createAction(
  '[NetworkNodesPage] Load',
  props<{ networkId: number }>()
);

export const actionNetworkNodesPageLoaded = createAction(
  '[NetworkNodesPage] Loaded',
  props<ApiResponse<NetworkNodesPage>>()
);

export const actionNetworkRoutesPageInit = createAction(
  '[NetworkRoutesPage] Init'
);

export const actionNetworkRoutesPageLoad = createAction(
  '[NetworkRoutesPage] Load',
  props<{ networkId: number }>()
);

export const actionNetworkRoutesPageLoaded = createAction(
  '[NetworkRoutesPage] Loaded',
  props<ApiResponse<NetworkRoutesPage>>()
);

export const actionNetworkFactsPageInit = createAction(
  '[NetworkFactsPage] Init'
);

export const actionNetworkFactsPageLoad = createAction(
  '[NetworkFactsPage] Load',
  props<{ networkId: number }>()
);

export const actionNetworkFactsPageLoaded = createAction(
  '[NetworkFactsPage] Loaded',
  props<ApiResponse<NetworkFactsPage>>()
);

export const actionNetworkMapPageInit = createAction('[NetworkMapPage] Init');

export const actionNetworkMapPageLoad = createAction(
  '[NetworkMapPage] Load',
  props<{ networkId: number; mapPositionFromUrl: NetworkMapPosition }>()
);

export const actionNetworkMapPageLoaded = createAction(
  '[NetworkMapPage] Loaded',
  props<{
    response: ApiResponse<NetworkMapPage>;
    mapPositionFromUrl: NetworkMapPosition;
  }>()
);

export const actionNetworkChangesPageInit = createAction(
  '[NetworkChangesPage] Init'
);

export const actionNetworkChangesLoad = createAction(
  '[NetworkChangesPage] Load',
  props<{ networkId: number; changesParameters: ChangesParameters }>()
);

export const actionNetworkChangesPageLoaded = createAction(
  '[NetworkChangesPage] Loaded',
  props<ApiResponse<NetworkChangesPage>>()
);

export const actionNetworkChangesPageSize = createAction(
  '[NetworkChangesPage] Page size',
  props<{ pageSize: number }>()
);

export const actionNetworkChangesPageIndex = createAction(
  '[NetworkChangesPage] Page index',
  props<{ pageIndex: number }>()
);

export const actionNetworkChangesImpact = createAction(
  '[NetworkChangesPage] Impact',
  props<{ impact: boolean }>()
);

export const actionNetworkChangesFilterOption = createAction(
  '[NetworkChangesPage] Filter option',
  props<{ option: ChangeOption }>()
);
