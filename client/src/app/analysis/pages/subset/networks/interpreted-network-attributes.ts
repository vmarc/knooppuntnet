import {NetworkAttributes} from "../../../../kpn/shared/network/network-attributes";

export class InterpretedNetworkAttributes {

  constructor(private network: NetworkAttributes) {
  }

  percentageOk(): string {
    return Math.round(this.routePercentageOk()) + "%";
  }

  happy(): boolean {
    return this.routePercentageOk() > 95;
  }

  veryHappy(): boolean {
    return this.routePercentageOk() > 99.9;
  }

  private routePercentageOk() {
    if (this.network.routeCount > 0) {
      return 100 * (this.network.routeCount - this.network.brokenRouteCount) / this.network.routeCount;
    }
    return 0;
  }

}
