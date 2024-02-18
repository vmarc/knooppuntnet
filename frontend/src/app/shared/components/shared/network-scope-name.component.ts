import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkScope } from '@api/custom';
import { Translations } from '@app/i18n';

@Component({
  selector: 'kpn-network-scope-name',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `{{ networkScopeName() }}`,
  standalone: true,
})
export class NetworkScopeNameComponent {
  networkScope = input.required<NetworkScope>();
  protected networkScopeName = computed(() =>
    Translations.get('network-scope.' + this.networkScope())
  );
}
