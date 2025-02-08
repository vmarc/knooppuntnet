import { Country } from '@api/common/country';

export class CountryName {
  constructor(
    readonly country: Country,
    readonly name: string
  ) {}
}
