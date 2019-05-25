import {Subset} from "../shared/subset";
import {Countries} from "./countries";
import {NetworkTypes} from "./network-types";

export class Subsets {

  static all = [
    new Subset(Countries.nl, NetworkTypes.cycling),
    new Subset(Countries.nl, NetworkTypes.hiking),
    new Subset(Countries.nl, NetworkTypes.horse),
    new Subset(Countries.nl, NetworkTypes.motorboat),
    new Subset(Countries.nl, NetworkTypes.canoe),
    new Subset(Countries.nl, NetworkTypes.inlineSkating),
    new Subset(Countries.be, NetworkTypes.cycling),
    new Subset(Countries.be, NetworkTypes.hiking),
    new Subset(Countries.be, NetworkTypes.horse),
    new Subset(Countries.de, NetworkTypes.cycling),
    new Subset(Countries.de, NetworkTypes.hiking),
    new Subset(Countries.de, NetworkTypes.horse)
  ];

}
