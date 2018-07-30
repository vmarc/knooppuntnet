// this class is generated, please do not modify

import {Country} from './country';
import {NetworkType} from './network-type';

export class Subset {

  constructor(public country?: Country,
              public networkType?: NetworkType) {
  }

  public static fromJSON(jsonObject): Subset {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new Subset();
    instance.country = jsonObject.country;
    instance.networkType = jsonObject.networkType;
    return instance;
  }
}

