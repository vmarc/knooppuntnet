// this file is generated, please do not modify

import { ChangeType } from '@api/common/change-type';
import { WayDiffs } from '@api/common/diff/way-diffs';
import { ChangeKey } from './change-key';

export interface BaseRouteChange {
  readonly _id: string;
  readonly key: ChangeKey;
  readonly changeType: ChangeType;
  readonly wayDiffs: WayDiffs;
}
