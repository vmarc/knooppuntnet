import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { NetworkScope } from '@api/custom';
import { I18nService } from '@app/i18n';

@Component({
  selector: 'kpn-network-scope-name',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `{{ networkScopeName() }}`,
  standalone: true,
})
export class NetworkScopeNameComponent {
  @Input() networkScope: NetworkScope;

  constructor(private i18nService: I18nService) {}

  networkScopeName(): string {
    return this.i18nService.translation('@@network-scope.' + this.networkScope);
  }
}
