import {Country} from './country';
import {NetworkType} from './network-type';

export class Subset {

  static all = [
    new Subset(Country.nl, NetworkType.cycling),
    new Subset(Country.nl, NetworkType.hiking),
    new Subset(Country.nl, NetworkType.horseRiding),
    new Subset(Country.nl, NetworkType.motorboat),
    new Subset(Country.nl, NetworkType.canoe),
    new Subset(Country.nl, NetworkType.inlineSkating),
    new Subset(Country.be, NetworkType.cycling),
    new Subset(Country.be, NetworkType.hiking),
    new Subset(Country.be, NetworkType.horseRiding),
    new Subset(Country.de, NetworkType.cycling),
    new Subset(Country.de, NetworkType.hiking),
    new Subset(Country.fr, NetworkType.cycling),
    new Subset(Country.fr, NetworkType.hiking),
    new Subset(Country.at, NetworkType.cycling),
    new Subset(Country.es, NetworkType.cycling)
  ];

  constructor(readonly country: Country,
              readonly networkType: NetworkType) {
  }

  public static fromJSON(jsonObject: any): Subset {
    if (!jsonObject) {
      return undefined;
    }
    const splitted = jsonObject.split(':');
    return new Subset(
      splitted[0],
      splitted[1]
    );
  }

  public key(): string {
    return this.networkType + '/' + this.country;
  }

}
