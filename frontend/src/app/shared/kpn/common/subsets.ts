import { Country } from '@api/custom';
import { NetworkType } from '@api/custom';
import { Subset } from '@api/custom';

export class Subsets {
  static all: Subset[] = [
    { country: Country.nl, networkType: 'cycling' },
    { country: Country.nl, networkType: 'hiking' },
    { country: Country.nl, networkType: 'horse-riding' },
    { country: Country.nl, networkType: 'motorboat' },
    { country: Country.nl, networkType: 'canoe' },
    { country: Country.nl, networkType: 'inline-skating' },
    { country: Country.be, networkType: 'cycling' },
    { country: Country.be, networkType: 'hiking' },
    { country: Country.be, networkType: 'horse-riding' },
    { country: Country.de, networkType: 'cycling' },
    { country: Country.de, networkType: 'hiking' },
    { country: Country.fr, networkType: 'cycling' },
    { country: Country.fr, networkType: 'hiking' },
    { country: Country.fr, networkType: 'horse-riding' },
    { country: Country.fr, networkType: 'canoe' },
    { country: Country.at, networkType: 'cycling' },
    { country: Country.es, networkType: 'cycling' },
    { country: Country.es, networkType: 'hiking' },
    { country: Country.dk, networkType: 'cycling' },
  ];

  static key(subset: Subset): string {
    return subset.networkType + '/' + subset.country;
  }
}
