// this file is generated, please do not modify

import { Bounds } from '@api/common/bounds';
import { ChangeType } from '@api/common/change-type';
import { WayDiffsInfo } from '@api/common/diff/way-diffs-info';
import { RouteDiff } from '@api/common/diff/route/route-diff';
import { GeometryDiff } from '@api/common/route/geometry-diff';
import { ChangeKey } from './change-key';

export interface BaseRouteChange {
  readonly _id: string;
  readonly key: ChangeKey;
  readonly changeType: ChangeType;
  readonly routeDiff: RouteDiff;
  readonly wayDiffs?: WayDiffsInfo;
  readonly geometryDiff?: GeometryDiff;
  readonly bounds?: Bounds;
}
