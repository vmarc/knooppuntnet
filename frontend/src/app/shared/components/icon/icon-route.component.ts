import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NzTooltipDirective } from 'ng-zorro-antd/tooltip';

@Component({
  selector: 'ui-icon-route',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nz-icon
      nzType="route"
      nz-tooltip
      i18n-nzTooltipTitle="@@icon.route.tooltip"
      nzTooltipTitle="route"
    />
  `,
  imports: [NzIconDirective, NzTooltipDirective],
})
export class IconRouteComponent {}
