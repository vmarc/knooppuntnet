// this class is generated, please do not modify

import {NetworkChange} from './network-change';

export class NetworkChangeDoc {

  constructor(public _id?: string,
              public networkChange?: NetworkChange,
              public _rev?: string) {
  }

  public static fromJSON(jsonObject): NetworkChangeDoc {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkChangeDoc();
    instance._id = jsonObject._id;
    instance.networkChange = jsonObject.networkChange;
    instance._rev = jsonObject._rev;
    return instance;
  }
}

