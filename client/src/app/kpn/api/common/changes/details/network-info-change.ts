// this file is generated, please do not modify

import { ChangeKey } from './change-key';
import { ChangeType } from './change-type';
import { Country } from '../../../custom/country';
import { IdDiffs } from '../../diff/id-diffs';
import { NetworkDataUpdate } from '../../diff/network-data-update';
import { NetworkType } from '../../../custom/network-type';
import { RefChanges } from './ref-changes';
import { RefDiffs } from '../../diff/ref-diffs';

export interface NetworkInfoChange {
  readonly _id: string;
  readonly key: ChangeKey;
  readonly changeType: ChangeType;
  readonly country: Country;
  readonly networkType: NetworkType;
  readonly networkId: number;
  readonly networkName: string;
  readonly orphanRouteDiffs: RefChanges;
  readonly orphanNodeDiffs: RefChanges;
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
