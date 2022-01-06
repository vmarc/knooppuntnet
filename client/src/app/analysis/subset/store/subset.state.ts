import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { SubsetChangesPage } from '@api/common/subset/subset-changes-page';
import { SubsetFactDetailsPage } from '@api/common/subset/subset-fact-details-page';
import { SubsetFactsPage } from '@api/common/subset/subset-facts-page';
import { SubsetInfo } from '@api/common/subset/subset-info';
import { SubsetMapPage } from '@api/common/subset/subset-map-page';
import { SubsetNetworksPage } from '@api/common/subset/subset-networks-page';
import { SubsetOrphanNodesPage } from '@api/common/subset/subset-orphan-nodes-page';
import { SubsetOrphanRoutesPage } from '@api/common/subset/subset-orphan-routes-page';
import { ApiResponse } from '@api/custom/api-response';
import { Subset } from '@api/custom/subset';

export class SubsetFact {
  constructor(readonly subset: Subset, readonly factName: string) {}
}

export const initialState: SubsetState = {
  subset: null,
  subsetInfo: null,
  networksPage: null,
  factsPage: null,
  subsetFact: null,
  factDetailsPage: null,
  orphanNodesPage: null,
  orphanRoutesPage: null,
  mapPage: null,
  changesPage: null,
  changesParameters: null,
};

export interface SubsetState {
  subset: Subset;
  subsetInfo: SubsetInfo;
  networksPage: ApiResponse<SubsetNetworksPage>;
  factsPage: ApiResponse<SubsetFactsPage>;
  subsetFact: SubsetFact;
  factDetailsPage: ApiResponse<SubsetFactDetailsPage>;
  orphanNodesPage: ApiResponse<SubsetOrphanNodesPage>;
  orphanRoutesPage: ApiResponse<SubsetOrphanRoutesPage>;
  mapPage: ApiResponse<SubsetMapPage>;
  changesPage: ApiResponse<SubsetChangesPage>;
  changesParameters: ChangesParameters;
}

export const subsetFeatureKey = 'subset';
