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
    instance.country = Country.fromJSON(jsonObject.country);
    instance.networkType = NetworkType.fromJSON(jsonObject.networkType);
    return instance;
  }
}

