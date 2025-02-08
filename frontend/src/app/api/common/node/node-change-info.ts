// this file is generated, please do not modify

import { ChangeType } from '@api/common/change-type';
import { Fact } from '@api/common/fact';
import { LatLonImpl } from '@api/common/lat-lon-impl';
import { ChangeKey } from '@api/common/changes/details/change-key';
import { RefBooleanChange } from '@api/common/changes/details/ref-boolean-change';
import { Ref } from '@api/common/common/ref';
import { MetaData } from '@api/common/data/meta-data';
import { TagDiffs } from '@api/common/diff/tag-diffs';
import { FactDiffs } from '@api/common/diff/common/fact-diffs';
import { NodeMoved } from '@api/common/diff/node/node-moved';
import { Tag } from '@api/custom/tag';

export interface NodeChangeInfo {
  readonly rowIndex: number;
  readonly id: number;
  readonly version: number;
  readonly changeKey: ChangeKey;
  readonly changeType: ChangeType;
  readonly changeTags: Tag[];
  readonly comment: string;
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
}
