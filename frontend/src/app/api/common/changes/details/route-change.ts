// this file is generated, please do not modify

import { ChangeType } from '@api/common/change-type';
import { Ref } from '@api/common/common/ref';
import { RawWay } from '@api/common/data/raw/raw-way';
import { RouteData } from '@api/common/diff/route-data';
import { RouteDiff } from '@api/common/diff/route/route-diff';
import { WayUpdate } from '@api/common/diff/way-update';
import { Fact } from '@api/common/fact';
import { RouteLocationAnalysis } from '@api/common/route-location-analysis';
import { ChangeKey } from './change-key';

export interface RouteChange {
  readonly _id: string;
  readonly key: ChangeKey;
  readonly changeType: ChangeType;
  readonly name: string;
  readonly locationAnalysis: RouteLocationAnalysis;
  readonly addedToNetwork: Ref[];
  readonly removedFromNetwork: Ref[];
  readonly before?: RouteData;
  readonly after?: RouteData;
  readonly removedWays: RawWay[];
  readonly addedWays: RawWay[];
  readonly updatedWays: WayUpdate[];
  readonly diffs: RouteDiff;
  readonly facts: Fact[];
  readonly impactedNodeIds: number[];
  readonly happy: boolean;
  readonly investigate: boolean;
  readonly impact: boolean;
  readonly locationHappy: boolean;
  readonly locationInvestigate: boolean;
  readonly locationImpact: boolean;
}
