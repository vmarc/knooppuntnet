import { Country } from '@api/custom/country';

export class Countries {
  static all: Array<Country> = [
    Country.nl,
    Country.be,
    Country.de,
    Country.fr,
    Country.at,
    Country.es,
    Country.dk,
  ];

  public static withDomain(domain: string): Country {
    return Countries.all.find((country) => country === domain);
  }
}
