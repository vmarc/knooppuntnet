// this file is generated, please do not modify

import { Bounds } from '@api/common/bounds';
import { ChangeType } from '@api/common/change-type';
import { ChangeSetInfo } from '@api/common/changes/change-set-info';
import { ChangeKey } from '@api/common/changes/details/change-key';
import { MetaData } from '@api/common/data/meta-data';
import { WayDiffsInfo } from '@api/common/diff/way-diffs-info';
import { RouteDiff } from '@api/common/diff/route/route-diff';
import { GeometryDiff } from './geometry-diff';
import { RouteNode } from './route-node';
import { RouteNodeChange } from './route-node-change';

export interface RouteChangeInfo {
  readonly rowIndex: number;
  readonly id: number;
  readonly version: number;
  readonly changeKey: ChangeKey;
  readonly changeType: ChangeType;
  readonly comment?: string;
  readonly before?: MetaData;
  readonly after?: MetaData;
  readonly diffs: RouteDiff;
  readonly nodes: RouteNode[];
  readonly nodeChanges: RouteNodeChange[];
  readonly changeSetInfo?: ChangeSetInfo;
  readonly wayDiffs?: WayDiffsInfo;
  readonly geometryDiff?: GeometryDiff;
  readonly bounds?: Bounds;
  readonly happy: boolean;
  readonly investigate: boolean;
}
