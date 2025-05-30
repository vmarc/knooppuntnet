import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NzTooltipDirective } from 'ng-zorro-antd/tooltip';

@Component({
  selector: 'ui-icon-way',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nz-icon
      nzType="way"
      nz-tooltip
      i18n-nzTooltipTitle="@@icon.way.tooltip"
      nzTooltipTitle="way"
    />
  `,
  imports: [NzIconDirective, NzTooltipDirective],
})
export class IconWayComponent {}
