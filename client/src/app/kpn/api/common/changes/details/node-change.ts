// this file is generated, please do not modify

import { ChangeKey } from './change-key';
import { ChangeType } from './change-type';
import { Fact } from '../../../custom/fact';
import { FactDiffs } from '../../diff/common/fact-diffs';
import { Location } from '../../location/location';
import { NodeMoved } from '../../diff/node/node-moved';
import { RawNode } from '../../data/raw/raw-node';
import { Ref } from '../../common/ref';
import { RefBooleanChange } from './ref-boolean-change';
import { Subset } from '../../../custom/subset';
import { TagDiffs } from '../../diff/tag-diffs';

export interface NodeChange {
  readonly _id: string;
  readonly key: ChangeKey;
  readonly changeType: ChangeType;
  readonly subsets: Subset[];
  readonly location: Location;
  readonly name: string;
  readonly before: RawNode;
  readonly after: RawNode;
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
  readonly happy: boolean;
  readonly investigate: boolean;
  readonly impact: boolean;
  readonly locationHappy: boolean;
  readonly locationInvestigate: boolean;
  readonly locationImpact: boolean;
  readonly comment: string;
}
