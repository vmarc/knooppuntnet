import { signal } from '@angular/core';
import { NetworkType } from '@api/custom';

export class PageState {
  private readonly _small = signal<boolean>(false);
  private readonly _activePanel = signal<string>('map');
  private readonly _networkType = signal<NetworkType>(NetworkType.hiking);

  readonly small = this._small.asReadonly();
  readonly activePanel = this._activePanel.asReadonly();
  readonly networkType = this._networkType.asReadonly();

  updateSmall(value: boolean): void {
    this._small.set(value);
  }

  updateActivePanel(value: string): void {
    this._activePanel.set(value);
  }

  updateNetworkType(value: NetworkType): void {
    this._networkType.set(value);
  }
}
