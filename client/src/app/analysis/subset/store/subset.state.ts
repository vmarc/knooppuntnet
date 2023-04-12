import { ChangesParameters } from '@api/common/changes/filter';
import { SubsetChangesPage } from '@api/common/subset';
import { SubsetFactDetailsPage } from '@api/common/subset';
import { SubsetFactsPage } from '@api/common/subset';
import { SubsetInfo } from '@api/common/subset';
import { SubsetMapPage } from '@api/common/subset';
import { SubsetNetworksPage } from '@api/common/subset';
import { SubsetOrphanNodesPage } from '@api/common/subset';
import { SubsetOrphanRoutesPage } from '@api/common/subset';
import { ApiResponse } from '@api/custom';
import { Subset } from '@api/custom';

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
