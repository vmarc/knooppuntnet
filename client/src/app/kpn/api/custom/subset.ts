import {Countries} from "../../common/countries";
import {Country} from "./country";
import {NetworkType} from "./network-type";

export class Subset {

  static all = [
    new Subset(Countries.nl, NetworkType.cycling),
    new Subset(Countries.nl, NetworkType.hiking),
    new Subset(Countries.nl, NetworkType.horseRiding),
    new Subset(Countries.nl, NetworkType.motorboat),
    new Subset(Countries.nl, NetworkType.canoe),
    new Subset(Countries.nl, NetworkType.inlineSkating),
    new Subset(Countries.be, NetworkType.cycling),
    new Subset(Countries.be, NetworkType.hiking),
    new Subset(Countries.be, NetworkType.horseRiding),
    new Subset(Countries.de, NetworkType.cycling),
    new Subset(Countries.de, NetworkType.hiking),
    new Subset(Countries.fr, NetworkType.cycling),
    new Subset(Countries.fr, NetworkType.hiking),
    new Subset(Countries.at, NetworkType.cycling),
    new Subset(Countries.es, NetworkType.cycling)
  ];

  constructor(readonly country: Country,
              readonly networkType: NetworkType) {
  }

  public static fromJSON(jsonObject: any): Subset {
    if (!jsonObject) {
      return undefined;
    }
    const splitted = jsonObject.split(":");
    return new Subset(
      Country.fromJSON(splitted[0]),
      NetworkType.fromJSON(splitted[1])
    );
  }

  public key(): string {
    return this.networkType.name + "/" + this.country.domain;
  }

}
