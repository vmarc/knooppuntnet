import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NzIconDirective } from 'ng-zorro-antd/icon';

@Component({
  selector: 'ui-icon-warning',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<nz-icon nzType="warning" nzTheme="twotone" nzTwotoneColor="#ff0000" />`,
  styles: `
    nz-icon {
      font-size: 22px;
    }
  `,
  imports: [NzIconDirective],
})
export class IconWarningComponent {}
