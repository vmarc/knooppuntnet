import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { NetworkScope } from '@api/custom/network-scope';
import { I18nService } from '@app/i18n/i18n.service';

@Component({
  selector: 'kpn-network-scope-name',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `{{ networkScopeName() }}`,
})
export class NetworkScopeNameComponent {
  @Input() networkScope: NetworkScope;

  constructor(private i18nService: I18nService) {}

  networkScopeName(): string {
    return this.i18nService.translation('@@network-scope.' + this.networkScope);
  }
}
