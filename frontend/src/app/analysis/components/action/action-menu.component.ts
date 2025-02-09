import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NzDropdownMenuComponent } from 'ng-zorro-antd/dropdown';
import { NzDropDownDirective } from 'ng-zorro-antd/dropdown';
import { NzIconDirective } from 'ng-zorro-antd/icon';

@Component({
  selector: 'kpn-action-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nz-icon nzType="open-in-new" class="action-button-icon" nz-dropdown [nzDropdownMenu]="menu" />
    <nz-dropdown-menu #menu="nzDropdownMenu">
      <ng-content />
    </nz-dropdown-menu>
  `,
  imports: [NzDropDownDirective, NzDropdownMenuComponent, NzIconDirective],
})
export class ActionMenuComponent {}
