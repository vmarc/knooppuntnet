// this file is generated, please do not modify

import { ChangeType } from '@api/common/change-type';
import { Ref } from '@api/common/common/ref';
import { RouteData } from '@api/common/diff/route-data';
import { RouteDiff } from '@api/common/diff/route/route-diff';
import { WayDiffs } from '@api/common/diff/way-diffs';
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
  readonly wayDiffs: WayDiffs;
  readonly diffs: RouteDiff;
  readonly facts: Fact[];
  readonly happy: boolean;
  readonly investigate: boolean;
  readonly impact: boolean;
  readonly locationHappy: boolean;
  readonly locationInvestigate: boolean;
  readonly locationImpact: boolean;
}
