// this file is generated, please do not modify

import { ChangeKey } from '@api/common/changes/details';
import { Timestamp } from '@api/custom';
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
