import { Location } from '@angular/common';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { NetworkSummary } from '@api/common/network';
import { RouterService } from '../../shared/services/router.service';

const defaultSummary: NetworkSummary = {
  name: '',
  networkType: null,
  networkScope: null,
  factCount: 0,
  nodeCount: 0,
  routeCount: 0,
  changeCount: 0,
};

@Injectable({
  providedIn: 'root',
})
export class NetworkService {
  private readonly location = inject(Location); // TODO SIGNAL move to RouterService

  private readonly _networkId = signal<number>(null);
  readonly networkId = this._networkId.asReadonly();

  private readonly _summary = signal<NetworkSummary>(defaultSummary);
  readonly summary = this._summary.asReadonly();

  initPage(routerService: RouterService): void {
    const networkIdString = routerService.param('networkId');
    const networkId = +networkIdString;
    const oldNetworkId = this.networkId();
    if (!oldNetworkId || oldNetworkId !== networkId) {
      let summary = defaultSummary;
      const state = this.location.getState();
      if (state) {
        const networkType = state['networkType'];
        const name = state['networkName'];
        summary = {
          ...defaultSummary,
          name,
          networkType,
        };
      }
      this._networkId.set(networkId);
      this._summary.set(summary);
    }
  }

  setSummary(summary: NetworkSummary): void {
    this._summary.set(summary);
  }
}
