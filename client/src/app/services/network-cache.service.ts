import {Injectable} from "@angular/core";
import {Map} from "immutable";
import {NetworkSummary} from "../kpn/shared/network/network-summary";

@Injectable({
  providedIn: "root"
})
export class NetworkCacheService {

  private networkNames = Map<string, string>();
  private networkSummaries = Map<string, NetworkSummary>();

  getNetworkName(networkId: string): string {
    return this.networkNames.get(networkId);
  }

  setNetworkName(networkId: string, networkName: string) {
    this.networkNames = this.networkNames.set(networkId, networkName);
  }

  getNetworkSummary(networkId: string): NetworkSummary {
    return this.networkSummaries.get(networkId);
  }

  setNetworkSummary(networkId: string, networkSummary: NetworkSummary) {
    this.networkSummaries = this.networkSummaries.set(networkId, networkSummary);
  }

}
