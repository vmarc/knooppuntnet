import { Country } from '@api/common';

export class CountryName {
  constructor(
    readonly country: Country,
    readonly name: string
  ) {}
}
