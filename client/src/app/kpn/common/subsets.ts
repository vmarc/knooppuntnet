import { Country } from '@api/custom/country';
import { NetworkType } from '@api/custom/network-type';
import { Subset } from '@api/custom/subset';

export class Subsets {
  static all: Subset[] = [
    { country: Country.nl, networkType: NetworkType.cycling },
    { country: Country.nl, networkType: NetworkType.hiking },
    { country: Country.nl, networkType: NetworkType.horseRiding },
    { country: Country.nl, networkType: NetworkType.motorboat },
    { country: Country.nl, networkType: NetworkType.canoe },
    { country: Country.nl, networkType: NetworkType.inlineSkating },
    { country: Country.be, networkType: NetworkType.cycling },
    { country: Country.be, networkType: NetworkType.hiking },
    { country: Country.be, networkType: NetworkType.horseRiding },
    { country: Country.de, networkType: NetworkType.cycling },
    { country: Country.de, networkType: NetworkType.hiking },
    { country: Country.fr, networkType: NetworkType.cycling },
    { country: Country.fr, networkType: NetworkType.hiking },
    { country: Country.at, networkType: NetworkType.cycling },
    { country: Country.es, networkType: NetworkType.cycling },
    { country: Country.es, networkType: NetworkType.hiking },
  ];

  static key(subset: Subset): string {
    return subset.networkType + '/' + subset.country;
  }
}
