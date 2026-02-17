import { Country } from '@api/common/country';

export class Countries {
  static all: Array<Country> = ['nl', 'be', 'de', 'fr', 'at', 'es', 'dk', 'pl'];

  public static withDomain(domain: string): Country {
    return Countries.all.find((country) => country === domain);
  }
}
