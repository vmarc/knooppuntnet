import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkType } from '@api/common';
import { Translations } from '@app/i18n';

@Component({
  selector: 'kpn-network-type-name',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `{{ networkTypeName() }}`,
})
export class NetworkTypeNameComponent {
  networkType = input.required<NetworkType>();

  protected networkTypeName = computed(() =>
    Translations.get('network-type.' + this.networkType())
  );
}
