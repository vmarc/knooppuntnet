// this class is generated, please do not modify

import {NetworkChange} from './network-change';

export class NetworkChangeDoc {
  readonly _id: string;
  readonly networkChange: NetworkChange;
  readonly _rev: string;

  constructor(_id: string,
              networkChange: NetworkChange,
              _rev: string) {
    this._id = _id;
    this.networkChange = networkChange;
    this._rev = _rev;
  }

  public static fromJSON(jsonObject): NetworkChangeDoc {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkChangeDoc(
      jsonObject._id,
      NetworkChange.fromJSON(jsonObject.networkChange),
      jsonObject._rev
    );
  }
}
