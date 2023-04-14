// this file is generated, please do not modify

import { LatLonImpl } from '@api/common';
import { Ref } from '@api/common/common';
import { MetaData } from '@api/common/data';
import { TagDiffs } from '@api/common/diff';
import { FactDiffs } from '@api/common/diff/common';
import { NodeMoved } from '@api/common/diff/node';
import { ChangeType } from '@api/custom';
import { Fact } from '@api/custom';
import { Subset } from '@api/custom';
import { Tags } from '@api/custom';
import { ChangeKey } from './change-key';
import { RefBooleanChange } from './ref-boolean-change';

export interface NodeChange {
  readonly _id: string;
  readonly key: ChangeKey;
  readonly changeType: ChangeType;
  readonly subsets: Subset[];
  readonly locations: string[];
  readonly name: string;
  readonly before: MetaData;
  readonly after: MetaData;
  readonly connectionChanges: RefBooleanChange[];
  readonly roleConnectionChanges: RefBooleanChange[];
  readonly definedInNetworkChanges: RefBooleanChange[];
  readonly tagDiffs: TagDiffs;
  readonly nodeMoved: NodeMoved;
  readonly addedToRoute: Ref[];
  readonly removedFromRoute: Ref[];
  readonly addedToNetwork: Ref[];
  readonly removedFromNetwork: Ref[];
  readonly factDiffs: FactDiffs;
  readonly facts: Fact[];
  readonly initialTags: Tags;
  readonly initialLatLon: LatLonImpl;
  readonly tiles: string[];
  readonly happy: boolean;
  readonly investigate: boolean;
  readonly impact: boolean;
  readonly locationHappy: boolean;
  readonly locationInvestigate: boolean;
  readonly locationImpact: boolean;
  readonly comment: string;
}
