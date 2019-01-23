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
    const splitted = jsonObject.split(":");
    return new Subset(
      Country.fromJSON(splitted[0]),
      NetworkType.fromJSON(splitted[1])
    );
  }
}
