import {SubsetChangesPage} from '@api/common/subset/subset-changes-page';
import {SubsetFactsPage} from '@api/common/subset/subset-facts-page';
import {SubsetMapPage} from '@api/common/subset/subset-map-page';
import {SubsetNetworksPage} from '@api/common/subset/subset-networks-page';
import {SubsetOrphanNodesPage} from '@api/common/subset/subset-orphan-nodes-page';
import {SubsetOrphanRoutesPage} from '@api/common/subset/subset-orphan-routes-page';
import {ApiResponse} from '@api/custom/api-response';
import {AppState} from '../../../core/core.state';

export const initialState: SubsetState = {
  networksPage: null,
  factsPage: null,
  orphanNodesPage: null,
  orphanRoutesPage: null,
  mapPage: null,
  changesPage: null
};

export interface SubsetState {
  networksPage: ApiResponse<SubsetNetworksPage>;
  factsPage: ApiResponse<SubsetFactsPage>;
  orphanNodesPage: ApiResponse<SubsetOrphanNodesPage>;
  orphanRoutesPage: ApiResponse<SubsetOrphanRoutesPage>;
  mapPage: ApiResponse<SubsetMapPage>;
  changesPage: ApiResponse<SubsetChangesPage>;
}

export const subsetFeatureKey = 'subset';

export interface SubsetRootState extends AppState {
  [subsetFeatureKey]: SubsetState;
}
