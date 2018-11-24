// this class is generated, please do not modify

import {List} from 'immutable';
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
  readonly key: ChangeKey;
  readonly changeType: ChangeType;
  readonly subsets: List<Subset>;
  readonly name: string;
  readonly before: RawNode;
  readonly after: RawNode;
  readonly connectionChanges: List<RefBooleanChange>;
  readonly roleConnectionChanges: List<RefBooleanChange>;
  readonly definedInNetworkChanges: List<RefBooleanChange>;
  readonly tagDiffs: TagDiffs;
  readonly nodeMoved: NodeMoved;
  readonly addedToRoute: List<Ref>;
  readonly removedFromRoute: List<Ref>;
  readonly addedToNetwork: List<Ref>;
  readonly removedFromNetwork: List<Ref>;
  readonly factDiffs: FactDiffs;
  readonly facts: List<Fact>;

  constructor(key: ChangeKey,
              changeType: ChangeType,
              subsets: List<Subset>,
              name: string,
              before: RawNode,
              after: RawNode,
              connectionChanges: List<RefBooleanChange>,
              roleConnectionChanges: List<RefBooleanChange>,
              definedInNetworkChanges: List<RefBooleanChange>,
              tagDiffs: TagDiffs,
              nodeMoved: NodeMoved,
              addedToRoute: List<Ref>,
              removedFromRoute: List<Ref>,
              addedToNetwork: List<Ref>,
              removedFromNetwork: List<Ref>,
              factDiffs: FactDiffs,
              facts: List<Fact>) {
    this.key = key;
    this.changeType = changeType;
    this.subsets = subsets;
    this.name = name;
    this.before = before;
    this.after = after;
    this.connectionChanges = connectionChanges;
    this.roleConnectionChanges = roleConnectionChanges;
    this.definedInNetworkChanges = definedInNetworkChanges;
    this.tagDiffs = tagDiffs;
    this.nodeMoved = nodeMoved;
    this.addedToRoute = addedToRoute;
    this.removedFromRoute = removedFromRoute;
    this.addedToNetwork = addedToNetwork;
    this.removedFromNetwork = removedFromNetwork;
    this.factDiffs = factDiffs;
    this.facts = facts;
  }

  public static fromJSON(jsonObject): NodeChange {
    if (!jsonObject) {
      return undefined;
    }
    return new NodeChange(
      ChangeKey.fromJSON(jsonObject.key),
      ChangeType.fromJSON(jsonObject.changeType),
      jsonObject.subsets ? List(jsonObject.subsets.map(json => Subset.fromJSON(json))) : List(),
      jsonObject.name,
      RawNode.fromJSON(jsonObject.before),
      RawNode.fromJSON(jsonObject.after),
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
      jsonObject.facts ? List(jsonObject.facts.map(json => Fact.fromJSON(json))) : List()
    );
  }
}
