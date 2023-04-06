// this file is generated, please do not modify

import { ChangeKey } from '../changes/details/change-key';
import { ChangeType } from '@api/custom/change-type';
import { Fact } from '@api/custom/fact';
import { FactDiffs } from '../diff/common/fact-diffs';
import { LatLonImpl } from '../lat-lon-impl';
import { MetaData } from '../data/meta-data';
import { NodeMoved } from '../diff/node/node-moved';
import { Ref } from '../common/ref';
import { RefBooleanChange } from '../changes/details/ref-boolean-change';
import { TagDiffs } from '../diff/tag-diffs';
import { Tags } from '@api/custom/tags';

export interface NodeChangeInfo {
  readonly rowIndex: number;
  readonly id: number;
  readonly version: number;
  readonly changeKey: ChangeKey;
  readonly changeType: ChangeType;
  readonly changeTags: Tags;
  readonly comment: string;
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
  readonly happy: boolean;
  readonly investigate: boolean;
}
