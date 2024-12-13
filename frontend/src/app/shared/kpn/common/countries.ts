import { Country } from '@api/common';

export class Countries {
  static all: Array<Country> = ['nl', 'be', 'de', 'fr', 'at', 'es', 'dk'];

  public static withDomain(domain: string): Country {
    return Countries.all.find((country) => country === domain);
  }
}
