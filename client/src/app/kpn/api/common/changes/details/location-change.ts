// this file is generated, please do not modify

import { ChangeKey } from './change-key';
import { NetworkType } from '../../../custom/network-type';
import { RefDiffs } from '../../diff/ref-diffs';

export interface LocationChange {
  readonly key: ChangeKey;
  readonly networkType: NetworkType;
  readonly locationName: string;
  readonly nodes: RefDiffs;
  readonly routes: RefDiffs;
  readonly happy: boolean;
  readonly investigate: boolean;
}
