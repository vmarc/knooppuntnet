import {Subset} from "../subset";
import {Country} from "../country";
import {NetworkType} from "../network-type";

export class Subsets {

  static all = [
    new Subset(new Country("nl"), new NetworkType("rcn")),
    new Subset(new Country("nl"), new NetworkType("rwn")),
    new Subset(new Country("nl"), new NetworkType("rhn")),
    new Subset(new Country("nl"), new NetworkType("rmn")),
    new Subset(new Country("nl"), new NetworkType("rpn")),
    new Subset(new Country("nl"), new NetworkType("rin")),
    new Subset(new Country("be"), new NetworkType("rcn")),
    new Subset(new Country("be"), new NetworkType("rwn")),
    new Subset(new Country("de"), new NetworkType("rcn")),
  ];

}
