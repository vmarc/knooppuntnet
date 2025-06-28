import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NzIconDirective } from 'ng-zorro-antd/icon';

@Component({
  selector: 'ui-icon-happy',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<nz-icon nzType="smile" theme="twotone" twoToneColor="#00ff00" />`,
  styles: `
    nz-icon {
      font-size: 22px;
    }
  `,
  imports: [NzIconDirective],
})
export class IconHappyComponent {}
