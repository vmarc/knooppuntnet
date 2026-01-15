import { Location } from '@angular/common';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { NetworkSummary } from '@api/common/network/network-summary';
import { NetworkPage } from '@app/analysis/network/internal/components/network-page';

const defaultSummary: NetworkSummary = {
  name: '',
  routeType: null,
  routeScope: null,
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

  private readonly _networkNotFound = signal(false);
  readonly networkNotFound = this._networkNotFound.asReadonly();

  private readonly _summary = signal<NetworkSummary>(defaultSummary);
  readonly summary = this._summary.asReadonly();

  private readonly _pageName = signal<NetworkPage>(null);
  readonly pageName = this._pageName.asReadonly();

  onInit(networkId: number) {
    const oldNetworkId = this.networkId();
    if (!oldNetworkId || oldNetworkId !== networkId) {
      let summary = defaultSummary;
      const state = this.location.getState();
      if (state) {
        const routeType = state['routeType'];
        const name = state['networkName'] ?? networkId.toString();
        summary = {
          ...defaultSummary,
          name,
          routeType,
        };
      }
      this._networkId.set(networkId);
      this._summary.set(summary);
    }
  }

  updatePageName(pageName: NetworkPage): void {
    this._pageName.set(pageName);
  }

  updateNetworkNotFound(networkNotFound: boolean): void {
    this._networkNotFound.set(networkNotFound);
  }

  setSummary(summary: NetworkSummary): void {
    this._summary.set(summary);
  }
}
