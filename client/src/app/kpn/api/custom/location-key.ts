import {Country} from "./country";
import {NetworkType} from "./network-type";

export class LocationKey {

  constructor(readonly networkType: NetworkType,
              readonly country: Country,
              readonly name: string) {
  }

  public static fromJSON(jsonObject: any): LocationKey {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationKey(
      NetworkType.fromJSON(jsonObject.networkType),
      Country.fromJSON(jsonObject.country),
      jsonObject.name
    );
  }

  public key(): string {
    return `${this.networkType.name}/${this.country.domain}/${this.name}`;
  }
}
