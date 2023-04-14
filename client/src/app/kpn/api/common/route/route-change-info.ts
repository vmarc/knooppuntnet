// this file is generated, please do not modify

import { Bounds } from '@api/common';
import { ChangeSetInfo } from '@api/common/changes';
import { ChangeKey } from '@api/common/changes/details';
import { MetaData } from '@api/common/data';
import { RawNode } from '@api/common/data/raw';
import { WayInfo } from '@api/common/diff';
import { WayUpdate } from '@api/common/diff';
import { RouteDiff } from '@api/common/diff/route';
import { ChangeType } from '@api/custom';
import { GeometryDiff } from './geometry-diff';

export interface RouteChangeInfo {
  readonly rowIndex: number;
  readonly id: number;
  readonly version: number;
  readonly changeKey: ChangeKey;
  readonly changeType: ChangeType;
  readonly comment: string;
  readonly before: MetaData;
  readonly after: MetaData;
  readonly removedWays: WayInfo[];
  readonly addedWays: WayInfo[];
  readonly updatedWays: WayUpdate[];
  readonly diffs: RouteDiff;
  readonly nodes: RawNode[];
  readonly changeSetInfo: ChangeSetInfo;
  readonly geometryDiff: GeometryDiff;
  readonly bounds: Bounds;
  readonly happy: boolean;
  readonly investigate: boolean;
}
