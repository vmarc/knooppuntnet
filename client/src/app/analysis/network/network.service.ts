import {Injectable} from "@angular/core";
import {ReplaySubject} from "rxjs";
import {NetworkSummary} from "../../kpn/api/common/network/network-summary";
import {NetworkCacheService} from "../../services/network-cache.service";

@Injectable({
  providedIn: "root",
})
export class NetworkService {

  networkName$ = new ReplaySubject<string>(1);
  networkSummary$ = new ReplaySubject<NetworkSummary>(1);

  constructor(private networkCacheService: NetworkCacheService) {
  }

  init(networkId: number): void {
    this.networkName$.next(this.networkCacheService.getNetworkName(networkId));
    this.networkSummary$.next(this.networkCacheService.getNetworkSummary(networkId));
  }

  update(networkId: number, networkSummary: NetworkSummary): void {
    this.networkCacheService.setNetworkName(networkId, networkSummary.name);
    this.networkCacheService.setNetworkSummary(networkId, networkSummary);
    this.networkName$.next(networkSummary.name);
    this.networkSummary$.next(networkSummary);
  }
}
