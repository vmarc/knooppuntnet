// this class is generated, please do not modify

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

export class NodeChange {
  constructor(
    readonly key: ChangeKey,
    readonly changeType: ChangeType,
    readonly subsets: Array<Subset>,
    readonly location: Location,
    readonly name: string,
    readonly before: RawNode,
    readonly after: RawNode,
    readonly connectionChanges: Array<RefBooleanChange>,
    readonly roleConnectionChanges: Array<RefBooleanChange>,
    readonly definedInNetworkChanges: Array<RefBooleanChange>,
    readonly tagDiffs: TagDiffs,
    readonly nodeMoved: NodeMoved,
    readonly addedToRoute: Array<Ref>,
    readonly removedFromRoute: Array<Ref>,
    readonly addedToNetwork: Array<Ref>,
    readonly removedFromNetwork: Array<Ref>,
    readonly factDiffs: FactDiffs,
    readonly facts: Array<Fact>,
    readonly happy: boolean,
    readonly investigate: boolean,
    readonly locationHappy: boolean,
    readonly locationInvestigate: boolean
  ) {}

  public static fromJSON(jsonObject: any): NodeChange {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeChange(
      ChangeKey.fromJSON(jsonObject.key),
      ChangeType.fromJSON(jsonObject.changeType),
      jsonObject.subsets.map((json: any) => Subset.fromJSON(json)),
      Location.fromJSON(jsonObject.location),
      jsonObject.name,
      RawNode.fromJSON(jsonObject.before),
      RawNode.fromJSON(jsonObject.after),
      jsonObject.connectionChanges.map((json: any) =>
        RefBooleanChange.fromJSON(json)
      ),
      jsonObject.roleConnectionChanges.map((json: any) =>
        RefBooleanChange.fromJSON(json)
      ),
      jsonObject.definedInNetworkChanges.map((json: any) =>
        RefBooleanChange.fromJSON(json)
      ),
      TagDiffs.fromJSON(jsonObject.tagDiffs),
      NodeMoved.fromJSON(jsonObject.nodeMoved),
      jsonObject.addedToRoute.map((json: any) => Ref.fromJSON(json)),
      jsonObject.removedFromRoute.map((json: any) => Ref.fromJSON(json)),
      jsonObject.addedToNetwork.map((json: any) => Ref.fromJSON(json)),
      jsonObject.removedFromNetwork.map((json: any) => Ref.fromJSON(json)),
      FactDiffs.fromJSON(jsonObject.factDiffs),
      jsonObject.facts,
      jsonObject.happy,
      jsonObject.investigate,
      jsonObject.locationHappy,
      jsonObject.locationInvestigate
    );
  }
}
