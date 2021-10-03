// this file is generated, please do not modify

import { ChangeKey } from './change-key';
import { ChangeType } from '../../../custom/change-type';
import { Fact } from '../../../custom/fact';
import { FactDiffs } from '../../diff/common/fact-diffs';
import { MetaData } from '../../data/meta-data';
import { NodeMoved } from '../../diff/node/node-moved';
import { Ref } from '../../common/ref';
import { RefBooleanChange } from './ref-boolean-change';
import { Subset } from '../../../custom/subset';
import { TagDiffs } from '../../diff/tag-diffs';

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
  readonly tiles: string[];
  readonly happy: boolean;
  readonly investigate: boolean;
  readonly impact: boolean;
  readonly locationHappy: boolean;
  readonly locationInvestigate: boolean;
  readonly locationImpact: boolean;
  readonly comment: string;
}
