// this class is generated, please do not modify

import {Country} from './country';
import {NetworkType} from './network-type';

export class Subset {
  readonly country: Country;
  readonly networkType: NetworkType;

  constructor(country: Country,
              networkType: NetworkType) {
    this.country = country;
    this.networkType = networkType;
  }

  public static fromJSON(jsonObject): Subset {
    if (!jsonObject) {
      return undefined;
    }
    return new Subset(
      Country.fromJSON(jsonObject.country),
      NetworkType.fromJSON(jsonObject.networkType)
    );
  }
}
