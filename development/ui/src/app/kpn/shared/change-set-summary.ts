// this class is generated, please do not modify

import {List} from 'immutable';
import {ChangeKey} from './changes/details/change-key';
import {ChangeSetSubsetElementRefs} from './change-set-subset-element-refs';
import {NetworkChanges} from './network-changes';
import {Subset} from './subset';
import {Timestamp} from './timestamp';

export class ChangeSetSummary {

  constructor(readonly key: ChangeKey,
              readonly subsets: List<Subset>,
              readonly timestampFrom: Timestamp,
              readonly timestampUntil: Timestamp,
              readonly networkChanges: NetworkChanges,
              readonly orphanRouteChanges: List<ChangeSetSubsetElementRefs>,
              readonly orphanNodeChanges: List<ChangeSetSubsetElementRefs>,
              readonly happy: boolean,
              readonly investigate: boolean) {
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
