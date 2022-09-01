import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { SubsetChangesPage } from '@api/common/subset/subset-changes-page';
import { SubsetFactDetailsPage } from '@api/common/subset/subset-fact-details-page';
import { SubsetFactRefs } from '@api/common/subset/subset-fact-refs';
import { SubsetFactsPage } from '@api/common/subset/subset-facts-page';
import { SubsetMapPage } from '@api/common/subset/subset-map-page';
import { SubsetNetworksPage } from '@api/common/subset/subset-networks-page';
import { SubsetOrphanNodesPage } from '@api/common/subset/subset-orphan-nodes-page';
import { SubsetOrphanRoutesPage } from '@api/common/subset/subset-orphan-routes-page';
import { ApiResponse } from '@api/custom/api-response';
import { Fact } from '@api/custom/fact';
import { Subset } from '@api/custom/subset';
import { props } from '@ngrx/store';
import { createAction } from '@ngrx/store';
import { ChangeOption } from '../../changes/store/changes.actions';
import { SubsetFact } from './subset.state';

export const actionSubsetNetworksPageInit = createAction(
  '[SubsetNetworksPage] Init'
);

export const actionSubsetNetworksPageLoad = createAction(
  '[SubsetNetworksPage] Load',
  props<{ subset: Subset }>()
);

export const actionSubsetNetworksPageLoaded = createAction(
  '[SubsetNetworksPage] Loaded',
  props<ApiResponse<SubsetNetworksPage>>()
);

export const actionSubsetFactsPageInit = createAction('[SubsetFactsPage] Init');

export const actionSubsetFactsPageLoad = createAction(
  '[SubsetFactsPage] Load',
  props<{ subset: Subset }>()
);

export const actionSubsetFactsPageLoaded = createAction(
  '[SubsetFactsPage] Loaded',
  props<ApiResponse<SubsetFactsPage>>()
);

export const actionSubsetFactRefsLoad = createAction(
  '[SubsetFactRefs] Load',
  props<{ fact: Fact }>()
);

export const actionSubsetFactRefsLoaded = createAction(
  '[SubsetFactRefs] Loaded',
  props<ApiResponse<SubsetFactRefs>>()
);

export const actionSubsetFactDetailsPageInit = createAction(
  '[SubsetFactDetailsPage] Init'
);

export const actionSubsetFactDetailsPageLoad = createAction(
  '[SubsetFactDetailsPage] Load',
  props<{ subsetFact: SubsetFact }>()
);

export const actionSubsetFactDetailsPageLoaded = createAction(
  '[SubsetFactDetailsPage] Loaded',
  props<ApiResponse<SubsetFactDetailsPage>>()
);

export const actionSubsetOrphanNodesPageInit = createAction(
  '[SubsetOrphanNodesPage] Init'
);

export const actionSubsetOrphanNodesPageLoad = createAction(
  '[SubsetOrphanNodesPage] Load',
  props<{ subset: Subset }>()
);

export const actionSubsetOrphanNodesPageLoaded = createAction(
  '[SubsetOrphanNodesPage] Loaded',
  props<ApiResponse<SubsetOrphanNodesPage>>()
);

export const actionSubsetOrphanRoutesPageInit = createAction(
  '[SubsetOrphanRoutesPage] Init'
);

export const actionSubsetOrphanRoutesPageLoad = createAction(
  '[SubsetOrphanRoutesPage] Load',
  props<{ subset: Subset }>()
);

export const actionSubsetOrphanRoutesPageLoaded = createAction(
  '[SubsetOrphanRoutesPage] Loaded',
  props<ApiResponse<SubsetOrphanRoutesPage>>()
);

export const actionSubsetMapPageInit = createAction('[SubsetMapPage] Init');

export const actionSubsetMapPageLoad = createAction(
  '[SubsetMapPage] Load',
  props<{ subset: Subset }>()
);

export const actionSubsetMapPageLoaded = createAction(
  '[SubsetMapPage] Loaded',
  props<ApiResponse<SubsetMapPage>>()
);

export const actionSubsetChangesPageInit = createAction(
  '[SubsetChangesPage] Init'
);

export const actionSubsetChangesPageLoad = createAction(
  '[SubsetChangesPage] Load',
  props<{ subset: Subset; changesParameters: ChangesParameters }>()
);

export const actionSubsetChangesPageLoaded = createAction(
  '[SubsetChangesPage] Loaded',
  props<ApiResponse<SubsetChangesPage>>()
);

export const actionSubsetChangesPageImpact = createAction(
  '[SubsetChangesPage] Impact',
  props<{ impact: boolean }>()
);

export const actionSubsetChangesPageSize = createAction(
  '[SubsetChangesPage] Page size',
  props<{ pageSize: number }>()
);

export const actionSubsetChangesPageIndex = createAction(
  '[SubsetChangesPage] Page index',
  props<{ pageIndex: number }>()
);

export const actionSubsetChangesFilterOption = createAction(
  '[SubsetChangesPage] Filter option',
  props<{ option: ChangeOption }>()
);
