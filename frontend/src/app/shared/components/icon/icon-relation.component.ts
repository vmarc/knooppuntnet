import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NzTooltipDirective } from 'ng-zorro-antd/tooltip';

@Component({
  selector: 'ui-icon-relation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nz-icon
      nzType="relation"
      nz-tooltip
      i18n-nzTooltipTitle="@@icon.relation.tooltip"
      nzTooltipTitle="relation"
    />
  `,
  imports: [NzIconDirective, NzTooltipDirective],
})
export class IconRelationComponent {}
