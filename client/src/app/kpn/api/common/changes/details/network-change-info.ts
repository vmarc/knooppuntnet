// this file is generated, please do not modify

import { ChangeKey } from './change-key';
import { ChangeType } from '@api/custom/change-type';
import { Country } from '@api/custom/country';
import { IdDiffs } from '../../diff/id-diffs';
import { MetaData } from '../../data/meta-data';
import { NetworkType } from '@api/custom/network-type';
import { RefDiffs } from '../../diff/ref-diffs';

export interface NetworkChangeInfo {
  readonly rowIndex: number;
  readonly comment: string;
  readonly key: ChangeKey;
  readonly changeType: ChangeType;
  readonly country: Country;
  readonly networkType: NetworkType;
  readonly networkId: number;
  readonly networkName: string;
  readonly before: MetaData;
  readonly after: MetaData;
  readonly networkDataUpdated: boolean;
  readonly networkNodes: RefDiffs;
  readonly routes: RefDiffs;
  readonly nodes: IdDiffs;
  readonly ways: IdDiffs;
  readonly relations: IdDiffs;
  readonly happy: boolean;
  readonly investigate: boolean;
}
