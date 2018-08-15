// this class is generated, please do not modify

import {ChangeKey} from './change-key';
import {ChangeType} from './change-type';
import {Fact} from '../../fact';
import {FactDiffs} from '../../diff/common/fact-diffs';
import {NodeMoved} from '../../diff/node/node-moved';
import {RawNode} from '../../data/raw/raw-node';
import {Ref} from '../../common/ref';
import {RefBooleanChange} from './ref-boolean-change';
import {Subset} from '../../subset';
import {TagDiffs} from '../../diff/tag-diffs';

export class NodeChange {

  constructor(public key?: ChangeKey,
              public changeType?: ChangeType,
              public subsets?: Array<Subset>,
              public name?: string,
              public before?: RawNode,
              public after?: RawNode,
              public connectionChanges?: Array<RefBooleanChange>,
              public definedInNetworkChanges?: Array<RefBooleanChange>,
              public tagDiffs?: TagDiffs,
              public nodeMoved?: NodeMoved,
              public addedToRoute?: Array<Ref>,
              public removedFromRoute?: Array<Ref>,
              public addedToNetwork?: Array<Ref>,
              public removedFromNetwork?: Array<Ref>,
              public factDiffs?: FactDiffs,
              public facts?: Array<Fact>) {
  }

  public static fromJSON(jsonObject): NodeChange {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NodeChange();
    instance.key = ChangeKey.fromJSON(jsonObject.key);
    instance.changeType = ChangeType.fromJSON(jsonObject.changeType);
    instance.subsets = jsonObject.subsets ? jsonObject.subsets.map(json => Subset.fromJSON(json)) : [];
    instance.name = jsonObject.name;
    instance.before = RawNode.fromJSON(jsonObject.before);
    instance.after = RawNode.fromJSON(jsonObject.after);
    instance.connectionChanges = jsonObject.connectionChanges ? jsonObject.connectionChanges.map(json => RefBooleanChange.fromJSON(json)) : [];
    instance.definedInNetworkChanges = jsonObject.definedInNetworkChanges ? jsonObject.definedInNetworkChanges.map(json => RefBooleanChange.fromJSON(json)) : [];
    instance.tagDiffs = TagDiffs.fromJSON(jsonObject.tagDiffs);
    instance.nodeMoved = NodeMoved.fromJSON(jsonObject.nodeMoved);
    instance.addedToRoute = jsonObject.addedToRoute ? jsonObject.addedToRoute.map(json => Ref.fromJSON(json)) : [];
    instance.removedFromRoute = jsonObject.removedFromRoute ? jsonObject.removedFromRoute.map(json => Ref.fromJSON(json)) : [];
    instance.addedToNetwork = jsonObject.addedToNetwork ? jsonObject.addedToNetwork.map(json => Ref.fromJSON(json)) : [];
    instance.removedFromNetwork = jsonObject.removedFromNetwork ? jsonObject.removedFromNetwork.map(json => Ref.fromJSON(json)) : [];
    instance.factDiffs = FactDiffs.fromJSON(jsonObject.factDiffs);
    instance.facts = jsonObject.facts ? jsonObject.facts.map(json => Fact.fromJSON(json)) : [];
    return instance;
  }
}

