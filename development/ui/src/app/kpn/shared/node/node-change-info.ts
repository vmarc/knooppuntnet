// this class is generated, please do not modify

import {List} from 'immutable';
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

  constructor(readonly id: number,
              readonly version: number,
              readonly changeKey: ChangeKey,
              readonly changeTags: Tags,
              readonly comment: string,
              readonly before: MetaData,
              readonly after: MetaData,
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
              readonly investigate: boolean) {
  }

  public static fromJSON(jsonObject): NodeChangeInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeChangeInfo(
      jsonObject.id,
      jsonObject.version,
      ChangeKey.fromJSON(jsonObject.changeKey),
      Tags.fromJSON(jsonObject.changeTags),
      jsonObject.comment,
      MetaData.fromJSON(jsonObject.before),
      MetaData.fromJSON(jsonObject.after),
      jsonObject.connectionChanges ? List(jsonObject.connectionChanges.map(json => RefBooleanChange.fromJSON(json))) : List(),
      jsonObject.roleConnectionChanges ? List(jsonObject.roleConnectionChanges.map(json => RefBooleanChange.fromJSON(json))) : List(),
      jsonObject.definedInNetworkChanges ? List(jsonObject.definedInNetworkChanges.map(json => RefBooleanChange.fromJSON(json))) : List(),
      TagDiffs.fromJSON(jsonObject.tagDiffs),
      NodeMoved.fromJSON(jsonObject.nodeMoved),
      jsonObject.addedToRoute ? List(jsonObject.addedToRoute.map(json => Ref.fromJSON(json))) : List(),
      jsonObject.removedFromRoute ? List(jsonObject.removedFromRoute.map(json => Ref.fromJSON(json))) : List(),
      jsonObject.addedToNetwork ? List(jsonObject.addedToNetwork.map(json => Ref.fromJSON(json))) : List(),
      jsonObject.removedFromNetwork ? List(jsonObject.removedFromNetwork.map(json => Ref.fromJSON(json))) : List(),
      FactDiffs.fromJSON(jsonObject.factDiffs),
      jsonObject.facts ? List(jsonObject.facts.map(json => Fact.fromJSON(json))) : List(),
      jsonObject.happy,
      jsonObject.investigate
    );
  }
}
