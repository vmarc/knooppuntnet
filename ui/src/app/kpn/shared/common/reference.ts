// this class is generated, please do not modify

import {NetworkType} from '../network-type';

export class Reference {

  constructor(public id?: number,
              public name?: string,
              public networkType?: NetworkType,
              public connection?: boolean) {
  }

  public static fromJSON(jsonObject): Reference {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new Reference();
    instance.id = jsonObject.id;
    instance.name = jsonObject.name;
    instance.networkType = jsonObject.networkType;
    instance.connection = jsonObject.connection;
    return instance;
  }
}

