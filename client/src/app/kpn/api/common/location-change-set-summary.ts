// this file is generated, please do not modify

import { Timestamp } from '@api/custom';
import { ChangeKey } from './changes/details';
import { LocationChangesTree } from './location-changes-tree';

export interface LocationChangeSetSummary {
  readonly _id: string;
  readonly key: ChangeKey;
  readonly timestampFrom: Timestamp;
  readonly timestampUntil: Timestamp;
  readonly trees: LocationChangesTree[];
  readonly happy: boolean;
  readonly investigate: boolean;
  readonly impact: boolean;
}
