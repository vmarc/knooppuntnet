// this file is generated, please do not modify

import { ChangeType } from '@api/common';
import { NetworkType } from '@api/common';
import { IdDiffs } from '@api/common/diff';
import { NetworkDataUpdate } from '@api/common/diff';
import { RefDiffs } from '@api/common/diff';
import { Country } from '@api/custom';
import { ChangeKey } from './change-key';

export interface NetworkInfoChange {
  readonly _id: string;
  readonly key: ChangeKey;
  readonly changeType: ChangeType;
  readonly country: Country;
  readonly networkType: NetworkType;
  readonly networkId: number;
  readonly networkName: string;
  readonly networkDataUpdate: NetworkDataUpdate;
  readonly nodeDiffs: RefDiffs;
  readonly routeDiffs: RefDiffs;
  readonly extraNodeDiffs: IdDiffs;
  readonly extraWayDiffs: IdDiffs;
  readonly extraRelationDiffs: IdDiffs;
  readonly happy: boolean;
  readonly investigate: boolean;
  readonly impact: boolean;
}
