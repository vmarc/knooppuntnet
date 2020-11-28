import {props} from '@ngrx/store';
import {createAction} from '@ngrx/store';
import {SubsetChangesPage} from '../../../kpn/api/common/subset/subset-changes-page';
import {SubsetFactsPage} from '../../../kpn/api/common/subset/subset-facts-page';
import {SubsetMapPage} from '../../../kpn/api/common/subset/subset-map-page';
import {SubsetNetworksPage} from '../../../kpn/api/common/subset/subset-networks-page';
import {SubsetOrphanNodesPage} from '../../../kpn/api/common/subset/subset-orphan-nodes-page';
import {SubsetOrphanRoutesPage} from '../../../kpn/api/common/subset/subset-orphan-routes-page';
import {ApiResponse} from '../../../kpn/api/custom/api-response';

export const actionSubsetNetworksLoaded = createAction(
  '[Subset] Networks loaded',
  props<{ response: ApiResponse<SubsetNetworksPage> }>()
);

export const actionSubsetFactsLoaded = createAction(
  '[Subset] Facts loaded',
  props<{ response: ApiResponse<SubsetFactsPage> }>()
);

export const actionSubsetOrphanNodesLoaded = createAction(
  '[Subset] Orphan nodes loaded',
  props<{ response: ApiResponse<SubsetOrphanNodesPage> }>()
);

export const actionSubsetOrphanRoutesLoaded = createAction(
  '[Subset] Orphan routes loaded',
  props<{ response: ApiResponse<SubsetOrphanRoutesPage> }>()
);

export const actionSubsetMapLoaded = createAction(
  '[Subset] Map loaded',
  props<{ response: ApiResponse<SubsetMapPage> }>()
);

export const actionSubsetChangesLoaded = createAction(
  '[Subset] Changes loaded',
  props<{ response: ApiResponse<SubsetChangesPage> }>()
);
