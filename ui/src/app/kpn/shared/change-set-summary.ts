// this class is generated, please do not modify

import {ChangeKey} from './changes/details/change-key';
import {ChangeSetSubsetElementRefs} from './change-set-subset-element-refs';
import {NetworkChanges} from './network-changes';
import {Subset} from './subset';
import {Timestamp} from './timestamp';

export class ChangeSetSummary {

  constructor(public key?: ChangeKey,
              public subsets?: Array<Subset>,
              public timestampFrom?: Timestamp,
              public timestampUntil?: Timestamp,
              public networkChanges?: NetworkChanges,
              public orphanRouteChanges?: Array<ChangeSetSubsetElementRefs>,
              public orphanNodeChanges?: Array<ChangeSetSubsetElementRefs>,
              public happy?: boolean,
              public investigate?: boolean) {
  }

  public static fromJSON(jsonObject): ChangeSetSummary {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new ChangeSetSummary();
    instance.key = jsonObject.key;
    instance.subsets = jsonObject.subsets ? jsonObject.subsets.map(json => Subset.fromJSON(json)) : [];
    instance.timestampFrom = jsonObject.timestampFrom;
    instance.timestampUntil = jsonObject.timestampUntil;
    instance.networkChanges = jsonObject.networkChanges;
    instance.orphanRouteChanges = jsonObject.orphanRouteChanges ? jsonObject.orphanRouteChanges.map(json => ChangeSetSubsetElementRefs.fromJSON(json)) : [];
    instance.orphanNodeChanges = jsonObject.orphanNodeChanges ? jsonObject.orphanNodeChanges.map(json => ChangeSetSubsetElementRefs.fromJSON(json)) : [];
    instance.happy = jsonObject.happy;
    instance.investigate = jsonObject.investigate;
    return instance;
  }
}

