// this file is generated, please do not modify

import { ChangeKey } from './changes/details/change-key';
import { LocationChangesTree } from './location-changes-tree';
import { Timestamp } from '../custom/timestamp';

export interface LocationChangeSetSummary {
  readonly key: ChangeKey;
  readonly timestampFrom: Timestamp;
  readonly timestampUntil: Timestamp;
  readonly trees: LocationChangesTree[];
  readonly happy: boolean;
  readonly investigate: boolean;
}
