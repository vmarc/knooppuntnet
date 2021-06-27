// this file is generated, please do not modify

import { ChangeKey } from './change-key';
import { ChangeType } from './change-type';
import { Country } from '../../../custom/country';
import { IdDiffs } from '../../diff/id-diffs';
import { NetworkDataUpdate } from '../../diff/network-data-update';
import { NetworkType } from '../../../custom/network-type';
import { RefChanges } from './ref-changes';
import { RefDiffs } from '../../diff/ref-diffs';

export interface NetworkChange {
  readonly _id: string;
  readonly key: ChangeKey;
  readonly changeType: ChangeType;
  readonly country: Country;
  readonly networkType: NetworkType;
  readonly networkId: number;
  readonly networkName: string;
  readonly orphanRoutes: RefChanges;
  readonly orphanNodes: RefChanges;
  readonly networkDataUpdate: NetworkDataUpdate;
  readonly networkNodes: RefDiffs;
  readonly routes: RefDiffs;
  readonly nodes: IdDiffs;
  readonly ways: IdDiffs;
  readonly relations: IdDiffs;
  readonly happy: boolean;
  readonly investigate: boolean;
  readonly impact: boolean;
}
