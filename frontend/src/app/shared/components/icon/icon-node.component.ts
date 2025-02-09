import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NzTooltipDirective } from 'ng-zorro-antd/tooltip';

@Component({
  selector: 'kpn-icon-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nz-icon
      nzType="node"
      nz-tooltip
      i18n-nzTooltipTitle="@@icon.node.tooltip"
      nzTooltipTitle="node"
    />
  `,
  imports: [NzIconDirective, NzTooltipDirective],
})
export class IconNodeComponent {}
