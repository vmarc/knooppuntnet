import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NzDropdownMenuComponent } from 'ng-zorro-antd/dropdown';
import { NzDropDownDirective } from 'ng-zorro-antd/dropdown';
import { NzIconDirective } from 'ng-zorro-antd/icon';

@Component({
  selector: 'ui-action-button',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a nz-dropdown [nzDropdownMenu]="nzDropdownMenu()">
      <nz-icon nzType="open-in-new" />
    </a>
  `,
  styles: `
    a > nz-icon {
      width: 12px !important;
      height: 12px !important;
    }
  `,
  imports: [NzDropDownDirective, NzIconDirective],
})
export class ActionButtonComponent {
  nzDropdownMenu = input.required<NzDropdownMenuComponent>();
}
