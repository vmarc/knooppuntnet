// this file is generated, please do not modify

import { RouteLocationAnalysis } from '@api/common';
import { Ref } from '@api/common/common';
import { RawWay } from '@api/common/data/raw';
import { RouteData } from '@api/common/diff';
import { WayUpdate } from '@api/common/diff';
import { RouteDiff } from '@api/common/diff/route';
import { ChangeType } from '@api/custom';
import { Fact } from '@api/custom';
import { ChangeKey } from './change-key';

export interface RouteChange {
  readonly _id: string;
  readonly key: ChangeKey;
  readonly changeType: ChangeType;
  readonly name: string;
  readonly locationAnalysis: RouteLocationAnalysis;
  readonly addedToNetwork: Ref[];
  readonly removedFromNetwork: Ref[];
  readonly before: RouteData;
  readonly after: RouteData;
  readonly removedWays: RawWay[];
  readonly addedWays: RawWay[];
  readonly updatedWays: WayUpdate[];
  readonly diffs: RouteDiff;
  readonly facts: Fact[];
  readonly impactedNodeIds: number[];
  readonly tiles: string[];
  readonly happy: boolean;
  readonly investigate: boolean;
  readonly impact: boolean;
  readonly locationHappy: boolean;
  readonly locationInvestigate: boolean;
  readonly locationImpact: boolean;
}
