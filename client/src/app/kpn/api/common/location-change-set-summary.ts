// this class is generated, please do not modify

import {ChangeKey} from './changes/details/change-key';
import {LocationChangesTree} from './location-changes-tree';
import {Timestamp} from '../custom/timestamp';

export class LocationChangeSetSummary {

  constructor(readonly key: ChangeKey,
              readonly timestampFrom: Timestamp,
              readonly timestampUntil: Timestamp,
              readonly trees: Array<LocationChangesTree>,
              readonly happy: boolean,
              readonly investigate: boolean) {
  }

  static fromJSON(jsonObject: any): LocationChangeSetSummary {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationChangeSetSummary(
      ChangeKey.fromJSON(jsonObject.key),
      Timestamp.fromJSON(jsonObject.timestampFrom),
      Timestamp.fromJSON(jsonObject.timestampUntil),
      jsonObject.trees?.map((json: any) => LocationChangesTree.fromJSON(json)),
      jsonObject.happy,
      jsonObject.investigate
    );
  }
}
