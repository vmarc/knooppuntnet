import { Subset } from '@api/custom/subset';

export class Subsets {
  static all: Subset[] = [
    { country: 'nl', routeType: 'cycling' },
    { country: 'nl', routeType: 'hiking' },
    { country: 'nl', routeType: 'horse-riding' },
    { country: 'nl', routeType: 'motorboat' },
    { country: 'nl', routeType: 'canoe' },
    { country: 'nl', routeType: 'inline-skating' },
    { country: 'be', routeType: 'cycling' },
    { country: 'be', routeType: 'hiking' },
    { country: 'be', routeType: 'horse-riding' },
    { country: 'de', routeType: 'cycling' },
    { country: 'de', routeType: 'hiking' },
    { country: 'fr', routeType: 'cycling' },
    { country: 'fr', routeType: 'hiking' },
    { country: 'fr', routeType: 'horse-riding' },
    { country: 'fr', routeType: 'canoe' },
    { country: 'at', routeType: 'cycling' },
    { country: 'es', routeType: 'cycling' },
    { country: 'es', routeType: 'hiking' },
    { country: 'dk', routeType: 'cycling' },
    { country: 'pl', routeType: 'cycling' },
    { country: 'pl', routeType: 'hiking' },
  ];

  static key(subset: Subset): string {
    return subset.routeType + '/' + subset.country;
  }
}
