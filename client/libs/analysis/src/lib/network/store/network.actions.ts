import { ChangesParameters } from '@api/common/changes/filter';
import { NetworkChangesPage } from '@api/common/network';
import { NetworkDetailsPage } from '@api/common/network';
import { NetworkFactsPage } from '@api/common/network';
import { NetworkMapPage } from '@api/common/network';
import { NetworkNodesPage } from '@api/common/network';
import { NetworkRoutesPage } from '@api/common/network';
import { ApiResponse } from '@api/custom';
import { NetworkType } from '@api/custom';
import { NetworkMapPosition } from '@app/components/ol/domain';
import { ChangeOption } from '@app/kpn/common';
import { props } from '@ngrx/store';
import { createAction } from '@ngrx/store';

export const actionNetworkDetailsPageInit = createAction(
  '[NetworkDetailsPage] Init'
);

export const actionNetworkDetailsPageLoad = createAction(
  '[NetworkDetailsPage] Load',
  props<{
    networkId: number;
    networkType: NetworkType | undefined;
    networkName: string | undefined;
  }>()
);

export const actionNetworkDetailsPageLoaded = createAction(
  '[NetworkDetailsPage] Loaded',
  props<ApiResponse<NetworkDetailsPage>>()
);

export const actionNetworkDetailsPageDestroy = createAction(
  '[NetworkDetailsPage] Destroy'
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

export const actionNetworkNodesPageDestroy = createAction(
  '[NetworkNodesPage] Destroy'
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

export const actionNetworkRoutesPageDestroy = createAction(
  '[NetworkRoutesPage] Destroy'
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

export const actionNetworkFactsPageDestroy = createAction(
  '[NetworkFactsPage] Destroy'
);

export const actionNetworkMapPageInit = createAction('[NetworkMapPage] Init');

export const actionNetworkMapPageLoad = createAction(
  '[NetworkMapPage] Load',
  props<{ networkId: number; mapPositionFromUrl: NetworkMapPosition }>()
);

export const actionNetworkMapViewInit = createAction(
  '[NetworkMapPage] ViewInit'
);

export const actionNetworkMapPageLoaded = createAction(
  '[NetworkMapPage] Loaded',
  props<{
    response: ApiResponse<NetworkMapPage>;
    mapPositionFromUrl: NetworkMapPosition;
  }>()
);

export const actionNetworkMapPageDestroy = createAction(
  '[NetworkMapPage] Destroy'
);

export const actionNetworkChangesPageInit = createAction(
  '[NetworkChangesPage] Init'
);

export const actionNetworkChangesPageLoad = createAction(
  '[NetworkChangesPage] Load',
  props<{ networkId: number; changesParameters: ChangesParameters }>()
);

export const actionNetworkChangesPageLoaded = createAction(
  '[NetworkChangesPage] Loaded',
  props<ApiResponse<NetworkChangesPage>>()
);

export const actionNetworkChangesPageDestroy = createAction(
  '[NetworkChangesPage] Destroy'
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
