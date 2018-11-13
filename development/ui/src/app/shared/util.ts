import {Params} from "@angular/router";
import {Subset} from "../kpn/shared/subset";
import {Country} from "../kpn/shared/country";
import {NetworkType} from "../kpn/shared/network-type";

export class Util {

  static subsetInRoute(params: Params): Subset {
    const country = params['country'];
    const networkType = params['networkType'];
    return new Subset(new Country(country), new NetworkType(networkType));
  }

}
