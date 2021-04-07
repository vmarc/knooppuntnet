import { Country } from '@api/custom/country';
import { NetworkType } from '@api/custom/network-type';
import { Subset } from '@api/custom/subset';

export class Subsets {
  static all = [
    new Subset(Country.nl, NetworkType.cycling),
    new Subset(Country.nl, NetworkType.hiking),
    new Subset(Country.nl, NetworkType.horseRiding),
    new Subset(Country.nl, NetworkType.motorboat),
    new Subset(Country.nl, NetworkType.canoe),
    new Subset(Country.nl, NetworkType.inlineSkating),
    new Subset(Country.be, NetworkType.cycling),
    new Subset(Country.be, NetworkType.hiking),
    new Subset(Country.be, NetworkType.horseRiding),
    new Subset(Country.de, NetworkType.cycling),
    new Subset(Country.de, NetworkType.hiking),
    new Subset(Country.fr, NetworkType.cycling),
    new Subset(Country.fr, NetworkType.hiking),
    new Subset(Country.at, NetworkType.cycling),
    new Subset(Country.es, NetworkType.cycling),
  ];

  static key(subset: Subset): string {
    return subset.networkType + '/' + subset.country;
  }
}
