import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkType } from '@api/custom';
import { I18nService } from '@app/i18n';

@Component({
  selector: 'kpn-network-type-name',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `{{ networkTypeName() }}`,
  standalone: true,
})
export class NetworkTypeNameComponent {
  networkType = input.required<NetworkType>();

  private readonly i18nService = inject(I18nService);

  networkTypeName(): string {
    return this.i18nService.translation('@@network-type.' + this.networkType());
  }
}
