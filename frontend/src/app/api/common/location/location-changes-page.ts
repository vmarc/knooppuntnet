// this file is generated, please do not modify

import { LocationChangeSetInfo } from '@api/common/location-change-set-info';
import { ChangesFilterOption } from '@api/common/changes/filter/changes-filter-option';
import { LocationSummary } from './location-summary';

export interface LocationChangesPage {
  readonly summary: LocationSummary;
  readonly changeSets: ReadonlyArray<LocationChangeSetInfo>;
  readonly changesCount: number;
  readonly filterOptions: ReadonlyArray<ChangesFilterOption>;
}
