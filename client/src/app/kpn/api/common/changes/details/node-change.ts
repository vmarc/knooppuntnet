// this file is generated, please do not modify

import { ChangeKey } from './change-key';
import { ChangeType } from '@api/custom/change-type';
import { Fact } from '@api/custom/fact';
import { FactDiffs } from '../../diff/common/fact-diffs';
import { LatLonImpl } from '../../lat-lon-impl';
import { MetaData } from '../../data/meta-data';
import { NodeMoved } from '../../diff/node/node-moved';
import { Ref } from '../../common/ref';
import { RefBooleanChange } from './ref-boolean-change';
import { Subset } from '@api/custom/subset';
import { TagDiffs } from '../../diff/tag-diffs';
import { Tags } from '@api/custom/tags';

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
