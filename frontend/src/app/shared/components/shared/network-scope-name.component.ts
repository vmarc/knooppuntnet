import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkScope } from '@api/custom';
import { I18nService } from '@app/i18n';

@Component({
  selector: 'kpn-network-scope-name',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `{{ networkScopeName() }}`,
  standalone: true,
})
export class NetworkScopeNameComponent {
  networkScope = input.required<NetworkScope>();

  private readonly i18nService = inject(I18nService);

  networkScopeName(): string {
    return this.i18nService.translation('@@network-scope.' + this.networkScope());
  }
}
