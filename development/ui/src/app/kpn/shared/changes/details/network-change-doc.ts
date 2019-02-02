// this class is generated, please do not modify

import {NetworkChange} from './network-change';

export class NetworkChangeDoc {

  constructor(readonly _id: string,
              readonly networkChange: NetworkChange,
              readonly _rev: string) {
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
