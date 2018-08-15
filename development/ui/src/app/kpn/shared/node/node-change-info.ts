// this class is generated, please do not modify

import {ChangeKey} from '../changes/details/change-key';
import {Fact} from '../fact';
import {FactDiffs} from '../diff/common/fact-diffs';
import {MetaData} from '../data/meta-data';
import {NodeMoved} from '../diff/node/node-moved';
import {Ref} from '../common/ref';
import {RefBooleanChange} from '../changes/details/ref-boolean-change';
import {TagDiffs} from '../diff/tag-diffs';
import {Tags} from '../data/tags';

export class NodeChangeInfo {

  constructor(public id?: number,
              public version?: number,
              public changeKey?: ChangeKey,
              public changeTags?: Tags,
              public comment?: string,
              public before?: MetaData,
              public after?: MetaData,
              public connectionChanges?: Array<RefBooleanChange>,
              public definedInNetworkChanges?: Array<RefBooleanChange>,
              public tagDiffs?: TagDiffs,
              public nodeMoved?: NodeMoved,
              public addedToRoute?: Array<Ref>,
              public removedFromRoute?: Array<Ref>,
              public addedToNetwork?: Array<Ref>,
              public removedFromNetwork?: Array<Ref>,
              public factDiffs?: FactDiffs,
              public facts?: Array<Fact>,
              public happy?: boolean,
              public investigate?: boolean) {
  }

  public static fromJSON(jsonObject): NodeChangeInfo {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NodeChangeInfo();
    instance.id = jsonObject.id;
    instance.version = jsonObject.version;
    instance.changeKey = ChangeKey.fromJSON(jsonObject.changeKey);
    instance.changeTags = Tags.fromJSON(jsonObject.changeTags);
    instance.comment = jsonObject.comment;
    instance.before = MetaData.fromJSON(jsonObject.before);
    instance.after = MetaData.fromJSON(jsonObject.after);
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
    instance.happy = jsonObject.happy;
    instance.investigate = jsonObject.investigate;
    return instance;
  }
}

