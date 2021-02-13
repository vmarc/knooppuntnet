import {Country} from './country';
import {NetworkType} from './network-type';

export class Subset {

  static all = [
    new Subset('nl', 'cycling'),
    new Subset('nl', 'hiking'),
    new Subset('nl', 'horse-riding'),
    new Subset('nl', 'motorboat'),
    new Subset('nl', 'canoe'),
    new Subset('nl', 'inline-skating'),
    new Subset('be', 'cycling'),
    new Subset('be', 'hiking'),
    new Subset('be', 'horse-riding'),
    new Subset('de', 'cycling'),
    new Subset('de', 'hiking'),
    new Subset('fr', 'cycling'),
    new Subset('fr', 'hiking'),
    new Subset('at', 'cycling'),
    new Subset('es', 'cycling')
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
