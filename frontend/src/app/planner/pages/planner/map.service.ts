import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { NetworkType } from '@api/custom';

@Injectable({
  providedIn: 'root',
})
export class MapService {
  private readonly _highlightedNodeId = signal<string>(null);
  private readonly _highlightedRouteId = signal<string>(null);
  private readonly _networkType = signal<NetworkType | null>(null);

  readonly highlightedNodeId = this._highlightedNodeId.asReadonly();
  readonly highlightedRouteId = this._highlightedRouteId.asReadonly();
  readonly networkType = this._networkType.asReadonly();

  setNetworkType(networkType: NetworkType): void {
    return this._networkType.set(networkType);
  }
}
