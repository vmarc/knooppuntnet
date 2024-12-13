import { Subset } from '@api/custom';

export class Subsets {
  static all: Subset[] = [
    { country: 'nl', networkType: 'cycling' },
    { country: 'nl', networkType: 'hiking' },
    { country: 'nl', networkType: 'horse-riding' },
    { country: 'nl', networkType: 'motorboat' },
    { country: 'nl', networkType: 'canoe' },
    { country: 'nl', networkType: 'inline-skating' },
    { country: 'be', networkType: 'cycling' },
    { country: 'be', networkType: 'hiking' },
    { country: 'be', networkType: 'horse-riding' },
    { country: 'de', networkType: 'cycling' },
    { country: 'de', networkType: 'hiking' },
    { country: 'fr', networkType: 'cycling' },
    { country: 'fr', networkType: 'hiking' },
    { country: 'fr', networkType: 'horse-riding' },
    { country: 'fr', networkType: 'canoe' },
    { country: 'at', networkType: 'cycling' },
    { country: 'es', networkType: 'cycling' },
    { country: 'es', networkType: 'hiking' },
    { country: 'dk', networkType: 'cycling' },
  ];

  static key(subset: Subset): string {
    return subset.networkType + '/' + subset.country;
  }
}
