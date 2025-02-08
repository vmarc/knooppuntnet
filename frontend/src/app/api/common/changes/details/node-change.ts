// this file is generated, please do not modify

import { ChangeType } from '@api/common/change-type';
import { Fact } from '@api/common/fact';
import { LatLonImpl } from '@api/common/lat-lon-impl';
import { Ref } from '@api/common/common/ref';
import { MetaData } from '@api/common/data/meta-data';
import { TagDiffs } from '@api/common/diff/tag-diffs';
import { FactDiffs } from '@api/common/diff/common/fact-diffs';
import { NodeMoved } from '@api/common/diff/node/node-moved';
import { Subset } from '@api/custom/subset';
import { Tag } from '@api/custom/tag';
import { ChangeKey } from './change-key';
import { RefBooleanChange } from './ref-boolean-change';

export interface NodeChange {
  readonly _id: string;
  readonly key: ChangeKey;
  readonly changeType: ChangeType;
  readonly subsets: Subset[];
  readonly locations: string[];
  readonly name: string;
  readonly before?: MetaData;
  readonly after?: MetaData;
  readonly connectionChanges: RefBooleanChange[];
  readonly roleConnectionChanges: RefBooleanChange[];
  readonly definedInNetworkChanges: RefBooleanChange[];
  readonly tagDiffs?: TagDiffs;
  readonly nodeMoved?: NodeMoved;
  readonly addedToRoute: Ref[];
  readonly removedFromRoute: Ref[];
  readonly addedToNetwork: Ref[];
  readonly removedFromNetwork: Ref[];
  readonly factDiffs?: FactDiffs;
  readonly facts: Fact[];
  readonly initialTags?: Tag[];
  readonly initialLatLon?: LatLonImpl;
  readonly happy: boolean;
  readonly investigate: boolean;
  readonly impact: boolean;
  readonly locationHappy: boolean;
  readonly locationInvestigate: boolean;
  readonly locationImpact: boolean;
  readonly comment: string;
}
