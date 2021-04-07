import { Country } from './country';
import { NetworkType } from './network-type';

export class LocationKey {
  constructor(
    readonly networkType: NetworkType,
    readonly country: Country,
    readonly name: string
  ) {}

  public static fromJSON(jsonObject: any): LocationKey {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationKey(
      jsonObject.networkType,
      jsonObject.country,
      jsonObject.name
    );
  }
}
