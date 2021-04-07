import { SubsetChangesPage } from '@api/common/subset/subset-changes-page';
import { SubsetFactsPage } from '@api/common/subset/subset-facts-page';
import { SubsetMapPage } from '@api/common/subset/subset-map-page';
import { SubsetNetworksPage } from '@api/common/subset/subset-networks-page';
import { SubsetOrphanNodesPage } from '@api/common/subset/subset-orphan-nodes-page';
import { SubsetOrphanRoutesPage } from '@api/common/subset/subset-orphan-routes-page';
import { ApiResponse } from '@api/custom/api-response';
import { props } from '@ngrx/store';
import { createAction } from '@ngrx/store';

export const actionSubsetNetworksPageInit = createAction(
  '[SubsetNetworksPage] Init'
);

export const actionSubsetNetworksPageLoaded = createAction(
  '[SubsetNetworksPage] Loaded',
  props<{ response: ApiResponse<SubsetNetworksPage> }>()
);

export const actionSubsetFactsPageInit = createAction('[SubsetFactsPage] Init');

export const actionSubsetFactsPageLoaded = createAction(
  '[SubsetFactsPage] Loaded',
  props<{ response: ApiResponse<SubsetFactsPage> }>()
);

export const actionSubsetOrphanNodesPageInit = createAction(
  '[SubsetOrphanNodesPage] Init'
);

export const actionSubsetOrphanNodesPageLoaded = createAction(
  '[SubsetOrphanNodesPage] Loaded',
  props<{ response: ApiResponse<SubsetOrphanNodesPage> }>()
);

export const actionSubsetOrphanRoutesPageInit = createAction(
  '[SubsetOrphanRoutesPage] Init'
);

export const actionSubsetOrphanRoutesPageLoaded = createAction(
  '[SubsetOrphanRoutesPage] Loaded',
  props<{ response: ApiResponse<SubsetOrphanRoutesPage> }>()
);

export const actionSubsetMapPageInit = createAction('[SubsetMapPage] Init');

export const actionSubsetMapPageLoaded = createAction(
  '[SubsetMapPage] Loaded',
  props<{ response: ApiResponse<SubsetMapPage> }>()
);

export const actionSubsetChangesPageInit = createAction(
  '[SubsetChangesPage] Init'
);

export const actionSubsetChangesPageLoaded = createAction(
  '[SubsetChangesPage] Loaded',
  props<{ response: ApiResponse<SubsetChangesPage> }>()
);
