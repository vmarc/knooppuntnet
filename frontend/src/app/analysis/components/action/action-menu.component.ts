import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NzDropdownMenuComponent } from 'ng-zorro-antd/dropdown';
import { NzDropDownDirective } from 'ng-zorro-antd/dropdown';
import { NzIconDirective } from 'ng-zorro-antd/icon';

@Component({
  selector: 'kpn-action-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a nz-dropdown [nzDropdownMenu]="menu">
      <nz-icon nzType="open-in-new" />
    </a>
    <nz-dropdown-menu #menu="nzDropdownMenu">
      <ng-content />
    </nz-dropdown-menu>
  `,
  styles: `
    a > nz-icon {
      width: 12px !important;
      height: 12px !important;
    }

    a {
      padding-left: 0.5em;
      padding-right: 0.5em;
    }
  `,
  imports: [NzDropDownDirective, NzDropdownMenuComponent, NzIconDirective],
})
export class ActionMenuComponent {}
