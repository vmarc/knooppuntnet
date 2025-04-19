// this file is generated, please do not modify

import { ChangesFilterOption } from '@api/common/changes/filter/changes-filter-option';
import { LocationChangeSetInfo } from '@api/common/location-change-set-info';
import { LocationSummary } from './location-summary';

export interface LocationChangesPage {
  readonly summary: LocationSummary;
  readonly changeSets: LocationChangeSetInfo[];
  readonly changesCount: number;
  readonly filterOptions: ChangesFilterOption[];
}
