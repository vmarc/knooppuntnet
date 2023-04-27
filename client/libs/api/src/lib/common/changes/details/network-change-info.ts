// this file is generated, please do not modify

import { MetaData } from '@api/common/data';
import { IdDiffs } from '@api/common/diff';
import { RefDiffs } from '@api/common/diff';
import { ChangeType } from '@api/custom';
import { Country } from '@api/custom';
import { NetworkType } from '@api/custom';
import { ChangeKey } from './change-key';

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
