import { Country } from '@api/custom/country';

export class CountryName {
  constructor(readonly country: Country, readonly name: string) {}
}
