import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ShowIfTruncatedDirective } from '@app/shared/components/format/show-if-truncated.directive';
import { NzTooltipDirective } from 'ng-zorro-antd/tooltip';

@Component({
  selector: 'ui-ellipsis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div #textContent class="kpn-ellipsis" nz-tooltip nzTooltipTitle="text" showIfTruncated>
      <ng-content />
    </div>
  `,
  imports: [NzTooltipDirective, ShowIfTruncatedDirective],
})
export class EllipsisComponent {}
