import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { NetworkType } from '@api/custom';
import { I18nService } from '@app/i18n';

@Component({
  selector: 'kpn-network-type-name',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `{{ networkTypeName() }}`,
  standalone: true,
})
export class NetworkTypeNameComponent {
  @Input() networkType: NetworkType;

  constructor(private i18nService: I18nService) {}

  networkTypeName(): string {
    return this.i18nService.translation('@@network-type.' + this.networkType);
  }
}
