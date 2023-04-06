// this file is generated, please do not modify

import { Bounds } from '../bounds';
import { ChangeKey } from '../changes/details/change-key';
import { ChangeSetInfo } from '../changes/change-set-info';
import { ChangeType } from '@api/custom/change-type';
import { GeometryDiff } from './geometry-diff';
import { MetaData } from '../data/meta-data';
import { RawNode } from '../data/raw/raw-node';
import { RouteDiff } from '../diff/route/route-diff';
import { WayInfo } from '../diff/way-info';
import { WayUpdate } from '../diff/way-update';

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
