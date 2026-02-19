// this file is generated, please do not modify

import { ChangeType } from '@api/common/change-type';
import { Fact } from '@api/common/fact';
import { RouteLocationAnalysis } from '@api/common/route-location-analysis';
import { Ref } from '@api/common/common/ref';
import { RouteData } from '@api/common/diff/route-data';
import { RouteNodeChange } from '@api/common/route/route-node-change';
import { ChangeKey } from './change-key';

export interface RouteChange {
  readonly _id: string;
  readonly key: ChangeKey;
  readonly changeType: ChangeType;
  readonly name: string;
  readonly locationAnalysis: RouteLocationAnalysis;
  readonly addedToNetwork: ReadonlyArray<Ref>;
  readonly removedFromNetwork: ReadonlyArray<Ref>;
  readonly before?: RouteData;
  readonly after?: RouteData;
  readonly nodeChanges: ReadonlyArray<RouteNodeChange>;
  readonly facts: ReadonlyArray<Fact>;
  readonly happy: boolean;
  readonly investigate: boolean;
  readonly impact: boolean;
  readonly locationHappy: boolean;
  readonly locationInvestigate: boolean;
  readonly locationImpact: boolean;
}
