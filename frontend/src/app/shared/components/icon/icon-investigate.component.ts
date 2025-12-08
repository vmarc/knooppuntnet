import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NzIconDirective } from 'ng-zorro-antd/icon';

@Component({
  selector: 'ui-icon-investigate',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<nz-icon nzType="meh" nzTheme="twotone" nzTwotoneColor="#ffff00" /> `,
  styles: `
    nz-icon {
      font-size: 22px;
    }
  `,
  imports: [NzIconDirective],
})
export class IconInvestigateComponent {}
