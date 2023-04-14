// this file is generated, please do not modify

import { LatLonImpl } from '@api/common';
import { ChangeKey } from '@api/common/changes/details';
import { RefBooleanChange } from '@api/common/changes/details';
import { Ref } from '@api/common/common';
import { MetaData } from '@api/common/data';
import { TagDiffs } from '@api/common/diff';
import { FactDiffs } from '@api/common/diff/common';
import { NodeMoved } from '@api/common/diff/node';
import { ChangeType } from '@api/custom';
import { Fact } from '@api/custom';
import { Tags } from '@api/custom';

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
