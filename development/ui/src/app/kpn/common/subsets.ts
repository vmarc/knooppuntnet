import {Subset} from "../shared/subset";
import {Countries} from "./countries";
import {NetworkTypes} from "./network-types";

export class Subsets {

  static all = [
    new Subset(Countries.nl, NetworkTypes.rcn),
    new Subset(Countries.nl, NetworkTypes.rwn),
    new Subset(Countries.nl, NetworkTypes.rhn),
    new Subset(Countries.nl, NetworkTypes.rmn),
    new Subset(Countries.nl, NetworkTypes.rpn),
    new Subset(Countries.nl, NetworkTypes.rin),
    new Subset(Countries.be, NetworkTypes.rcn),
    new Subset(Countries.be, NetworkTypes.rwn),
    new Subset(Countries.de, NetworkTypes.rcn)
  ];

}
