// this class is generated, please do not modify

import {ChangeKey} from '../changes/details/change-key';
import {Fact} from '../../custom/fact';
import {FactDiffs} from '../diff/common/fact-diffs';
import {MetaData} from '../data/meta-data';
import {NodeMoved} from '../diff/node/node-moved';
import {Ref} from '../common/ref';
import {RefBooleanChange} from '../changes/details/ref-boolean-change';
import {TagDiffs} from '../diff/tag-diffs';
import {Tags} from '../../custom/tags';

export class NodeChangeInfo {

  constructor(readonly id: number,
              readonly version: number,
              readonly changeKey: ChangeKey,
              readonly changeTags: Tags,
              readonly comment: string,
              readonly before: MetaData,
              readonly after: MetaData,
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
              readonly investigate: boolean) {
  }

  public static fromJSON(jsonObject: any): NodeChangeInfo {
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
      jsonObject.connectionChanges.map((json: any) => RefBooleanChange.fromJSON(json)),
      jsonObject.roleConnectionChanges.map((json: any) => RefBooleanChange.fromJSON(json)),
      jsonObject.definedInNetworkChanges.map((json: any) => RefBooleanChange.fromJSON(json)),
      TagDiffs.fromJSON(jsonObject.tagDiffs),
      NodeMoved.fromJSON(jsonObject.nodeMoved),
      jsonObject.addedToRoute.map((json: any) => Ref.fromJSON(json)),
      jsonObject.removedFromRoute.map((json: any) => Ref.fromJSON(json)),
      jsonObject.addedToNetwork.map((json: any) => Ref.fromJSON(json)),
      jsonObject.removedFromNetwork.map((json: any) => Ref.fromJSON(json)),
      FactDiffs.fromJSON(jsonObject.factDiffs),
      jsonObject.facts.map((json: any) => Fact.fromJSON(json)),
      jsonObject.happy,
      jsonObject.investigate
    );
  }
}
