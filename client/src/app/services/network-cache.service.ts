import {Injectable} from "@angular/core";
import {Map} from "immutable";
import {NetworkSummary} from "../kpn/api/common/network/network-summary";

@Injectable({
  providedIn: "root"
})
export class NetworkCacheService {

  private networkNames = Map<number, string>();
  private networkSummaries = Map<number, NetworkSummary>();

  getNetworkName(networkId: number): string {
    return this.networkNames.get(networkId);
  }

  setNetworkName(networkId: number, networkName: string) {
    this.networkNames = this.networkNames.set(networkId, networkName);
  }

  getNetworkSummary(networkId: number): NetworkSummary {
    return this.networkSummaries.get(networkId);
  }

  setNetworkSummary(networkId: number, networkSummary: NetworkSummary) {
    this.networkSummaries = this.networkSummaries.set(networkId, networkSummary);
  }

}
