import {Country} from './country';
import {NetworkType} from './network-type';

export class Subset {

  static all = [
    new Subset(Country.nl, 'cycling'),
    new Subset(Country.nl, 'hiking'),
    new Subset(Country.nl, 'horse-riding'),
    new Subset(Country.nl, 'motorboat'),
    new Subset(Country.nl, 'canoe'),
    new Subset(Country.nl, 'inline-skating'),
    new Subset(Country.be, 'cycling'),
    new Subset(Country.be, 'hiking'),
    new Subset(Country.be, 'horse-riding'),
    new Subset(Country.de, 'cycling'),
    new Subset(Country.de, 'hiking'),
    new Subset(Country.fr, 'cycling'),
    new Subset(Country.fr, 'hiking'),
    new Subset(Country.at, 'cycling'),
    new Subset(Country.es, 'cycling')
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
