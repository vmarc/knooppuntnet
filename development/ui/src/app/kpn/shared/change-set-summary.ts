// this class is generated, please do not modify

import {List} from 'immutable';
import {ChangeKey} from './changes/details/change-key';
import {ChangeSetSubsetElementRefs} from './change-set-subset-element-refs';
import {NetworkChanges} from './network-changes';
import {Subset} from './subset';
import {Timestamp} from './timestamp';

export class ChangeSetSummary {
  readonly key: ChangeKey;
  readonly subsets: List<Subset>;
  readonly timestampFrom: Timestamp;
  readonly timestampUntil: Timestamp;
  readonly networkChanges: NetworkChanges;
  readonly orphanRouteChanges: List<ChangeSetSubsetElementRefs>;
  readonly orphanNodeChanges: List<ChangeSetSubsetElementRefs>;
  readonly happy: boolean;
  readonly investigate: boolean;

  constructor(key: ChangeKey,
              subsets: List<Subset>,
              timestampFrom: Timestamp,
              timestampUntil: Timestamp,
              networkChanges: NetworkChanges,
              orphanRouteChanges: List<ChangeSetSubsetElementRefs>,
              orphanNodeChanges: List<ChangeSetSubsetElementRefs>,
              happy: boolean,
              investigate: boolean) {
    this.key = key;
    this.subsets = subsets;
    this.timestampFrom = timestampFrom;
    this.timestampUntil = timestampUntil;
    this.networkChanges = networkChanges;
    this.orphanRouteChanges = orphanRouteChanges;
    this.orphanNodeChanges = orphanNodeChanges;
    this.happy = happy;
    this.investigate = investigate;
  }

  public static fromJSON(jsonObject): ChangeSetSummary {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangeSetSummary(
      ChangeKey.fromJSON(jsonObject.key),
      jsonObject.subsets ? List(jsonObject.subsets.map(json => Subset.fromJSON(json))) : List(),
      Timestamp.fromJSON(jsonObject.timestampFrom),
      Timestamp.fromJSON(jsonObject.timestampUntil),
      NetworkChanges.fromJSON(jsonObject.networkChanges),
      jsonObject.orphanRouteChanges ? List(jsonObject.orphanRouteChanges.map(json => ChangeSetSubsetElementRefs.fromJSON(json))) : List(),
      jsonObject.orphanNodeChanges ? List(jsonObject.orphanNodeChanges.map(json => ChangeSetSubsetElementRefs.fromJSON(json))) : List(),
      jsonObject.happy,
      jsonObject.investigate
    );
  }
}
