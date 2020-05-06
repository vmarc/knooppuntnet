// this class is generated, please do not modify

import {List} from "immutable";
import {ChangeKey} from "./change-key";
import {ChangeType} from "./change-type";
import {Fact} from "../../../custom/fact";
import {FactDiffs} from "../../diff/common/fact-diffs";
import {Location} from "../../location/location";
import {NodeMoved} from "../../diff/node/node-moved";
import {RawNode} from "../../data/raw/raw-node";
import {Ref} from "../../common/ref";
import {RefBooleanChange} from "./ref-boolean-change";
import {Subset} from "../../../custom/subset";
import {TagDiffs} from "../../diff/tag-diffs";

export class NodeChange {

  constructor(readonly key: ChangeKey,
              readonly changeType: ChangeType,
              readonly subsets: List<Subset>,
              readonly location: Location,
              readonly name: string,
              readonly before: RawNode,
              readonly after: RawNode,
              readonly connectionChanges: List<RefBooleanChange>,
              readonly roleConnectionChanges: List<RefBooleanChange>,
              readonly definedInNetworkChanges: List<RefBooleanChange>,
              readonly tagDiffs: TagDiffs,
              readonly nodeMoved: NodeMoved,
              readonly addedToRoute: List<Ref>,
              readonly removedFromRoute: List<Ref>,
              readonly addedToNetwork: List<Ref>,
              readonly removedFromNetwork: List<Ref>,
              readonly factDiffs: FactDiffs,
              readonly facts: List<Fact>,
              readonly happy: boolean,
              readonly investigate: boolean,
              readonly locationHappy: boolean,
              readonly locationInvestigate: boolean) {
  }

  public static fromJSON(jsonObject: any): NodeChange {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeChange(
      ChangeKey.fromJSON(jsonObject.key),
      ChangeType.fromJSON(jsonObject.changeType),
      jsonObject.subsets ? List(jsonObject.subsets.map((json: any) => Subset.fromJSON(json))) : List(),
      Location.fromJSON(jsonObject.location),
      jsonObject.name,
      RawNode.fromJSON(jsonObject.before),
      RawNode.fromJSON(jsonObject.after),
      jsonObject.connectionChanges ? List(jsonObject.connectionChanges.map((json: any) => RefBooleanChange.fromJSON(json))) : List(),
      jsonObject.roleConnectionChanges ? List(jsonObject.roleConnectionChanges.map((json: any) => RefBooleanChange.fromJSON(json))) : List(),
      jsonObject.definedInNetworkChanges ? List(jsonObject.definedInNetworkChanges.map((json: any) => RefBooleanChange.fromJSON(json))) : List(),
      TagDiffs.fromJSON(jsonObject.tagDiffs),
      NodeMoved.fromJSON(jsonObject.nodeMoved),
      jsonObject.addedToRoute ? List(jsonObject.addedToRoute.map((json: any) => Ref.fromJSON(json))) : List(),
      jsonObject.removedFromRoute ? List(jsonObject.removedFromRoute.map((json: any) => Ref.fromJSON(json))) : List(),
      jsonObject.addedToNetwork ? List(jsonObject.addedToNetwork.map((json: any) => Ref.fromJSON(json))) : List(),
      jsonObject.removedFromNetwork ? List(jsonObject.removedFromNetwork.map((json: any) => Ref.fromJSON(json))) : List(),
      FactDiffs.fromJSON(jsonObject.factDiffs),
      jsonObject.facts ? List(jsonObject.facts.map((json: any) => Fact.fromJSON(json))) : List(),
      jsonObject.happy,
      jsonObject.investigate,
      jsonObject.locationHappy,
      jsonObject.locationInvestigate
    );
  }
}
