import {Country} from '@api/custom/country';

export class Countries {

  static nl: Country = 'nl';
  static be: Country = 'be';
  static de: Country = 'de';
  static fr: Country = 'fr';
  static at: Country = 'at';
  static es: Country = 'es';

  static all: Array<Country> = [
    Countries.nl,
    Countries.be,
    Countries.de,
    Countries.fr,
    Countries.at,
    Countries.es
  ];

  public static withDomain(domain: string): Country {
    return Countries.all.find(country => country === domain);
  }

}
