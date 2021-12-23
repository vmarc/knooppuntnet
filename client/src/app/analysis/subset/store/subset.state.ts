import { SubsetChangesPage } from '@api/common/subset/subset-changes-page';
import { SubsetFactsPage } from '@api/common/subset/subset-facts-page';
import { SubsetInfo } from '@api/common/subset/subset-info';
import { SubsetMapPage } from '@api/common/subset/subset-map-page';
import { SubsetNetworksPage } from '@api/common/subset/subset-networks-page';
import { SubsetOrphanNodesPage } from '@api/common/subset/subset-orphan-nodes-page';
import { SubsetOrphanRoutesPage } from '@api/common/subset/subset-orphan-routes-page';
import { ApiResponse } from '@api/custom/api-response';
import { Subset } from '@api/custom/subset';

export const initialState: SubsetState = {
  subset: null,
  subsetInfo: null,
  networksPage: null,
  factsPage: null,
  orphanNodesPage: null,
  orphanRoutesPage: null,
  mapPage: null,
  changesPage: null,
};

export interface SubsetState {
  subset: Subset;
  subsetInfo: SubsetInfo;
  networksPage: ApiResponse<SubsetNetworksPage>;
  factsPage: ApiResponse<SubsetFactsPage>;
  orphanNodesPage: ApiResponse<SubsetOrphanNodesPage>;
  orphanRoutesPage: ApiResponse<SubsetOrphanRoutesPage>;
  mapPage: ApiResponse<SubsetMapPage>;
  changesPage: ApiResponse<SubsetChangesPage>;
}

export const subsetFeatureKey = 'subset';
