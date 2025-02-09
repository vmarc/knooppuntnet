import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NzTooltipDirective } from 'ng-zorro-antd/tooltip';

@Component({
  selector: 'kpn-icon-network',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nz-icon
      nzType="network"
      nz-tooltip
      i18n-nzTooltipTitle="@@icon.network.tooltip"
      nzTooltipTitle="network"
    />
  `,
  imports: [NzIconDirective, NzTooltipDirective],
})
export class IconNetworkComponent {}
