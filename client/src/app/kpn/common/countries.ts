import {Country} from '@api/custom/country';

export class Countries {

  static all: Array<Country> = [
    'nl',
    'be',
    'de',
    'fr',
    'at',
    'es'
  ];

  public static withDomain(domain: string): Country {
    return Countries.all.find(country => country === domain);
  }

}
