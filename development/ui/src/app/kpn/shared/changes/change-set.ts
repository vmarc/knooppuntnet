// this class is generated, please do not modify

import {List} from 'immutable';
import {Change} from './change';
import {Timestamp} from '../timestamp';

export class ChangeSet {
  readonly id: number;
  readonly timestamp: Timestamp;
  readonly timestampFrom: Timestamp;
  readonly timestampUntil: Timestamp;
  readonly timestampBefore: Timestamp;
  readonly timestampAfter: Timestamp;
  readonly changes: List<Change>;

  constructor(id: number,
              timestamp: Timestamp,
              timestampFrom: Timestamp,
              timestampUntil: Timestamp,
              timestampBefore: Timestamp,
              timestampAfter: Timestamp,
              changes: List<Change>) {
    this.id = id;
    this.timestamp = timestamp;
    this.timestampFrom = timestampFrom;
    this.timestampUntil = timestampUntil;
    this.timestampBefore = timestampBefore;
    this.timestampAfter = timestampAfter;
    this.changes = changes;
  }

  public static fromJSON(jsonObject): ChangeSet {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangeSet(
      jsonObject.id,
      Timestamp.fromJSON(jsonObject.timestamp),
      Timestamp.fromJSON(jsonObject.timestampFrom),
      Timestamp.fromJSON(jsonObject.timestampUntil),
      Timestamp.fromJSON(jsonObject.timestampBefore),
      Timestamp.fromJSON(jsonObject.timestampAfter),
      jsonObject.changes ? List(jsonObject.changes.map(json => Change.fromJSON(json))) : List()
    );
  }
}
