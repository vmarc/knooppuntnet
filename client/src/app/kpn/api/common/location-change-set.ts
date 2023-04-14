// this file is generated, please do not modify

import { ChangeKey } from './changes/details';
import { LocationChanges } from './location-changes';

export interface LocationChangeSet {
  readonly _id: string;
  readonly key: ChangeKey;
  readonly locationChanges: LocationChanges[];
}
