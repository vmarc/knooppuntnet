// this file is generated, please do not modify

import { ChangeKey } from './change-key';
import { ChangeType } from './change-type';
import { Fact } from '../../../custom/fact';
import { RawWay } from '../../data/raw/raw-way';
import { Ref } from '../../common/ref';
import { RouteData } from '../../diff/route-data';
import { RouteDiff } from '../../diff/route/route-diff';
import { RouteLocationAnalysis } from '../../route-location-analysis';
import { WayUpdate } from '../../diff/way-update';

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
  readonly happy: boolean;
  readonly investigate: boolean;
  readonly impact: boolean;
  readonly locationHappy: boolean;
  readonly locationInvestigate: boolean;
  readonly locationImpact: boolean;
}
