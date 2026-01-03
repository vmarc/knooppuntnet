// this file is generated, please do not modify

import { ChangeType } from '@api/common/change-type';
import { MetaData } from '@api/common/data/meta-data';
import { WayDiffsInfo } from '@api/common/diff/way-diffs-info';
import { RouteDiff } from '@api/common/diff/route/route-diff';
import { GeometryDiff } from '@api/common/route/geometry-diff';
import { ChangeKey } from './change-key';

export interface BaseRouteChange {
  readonly _id: string;
  readonly key: ChangeKey;
  readonly changeType: ChangeType;
  readonly before?: MetaData;
  readonly after?: MetaData;
  readonly routeDiff: RouteDiff;
  readonly wayDiffs?: WayDiffsInfo;
  readonly geometryDiff?: GeometryDiff;
}
