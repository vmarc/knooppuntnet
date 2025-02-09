import { output } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NzMenuItemComponent } from 'ng-zorro-antd/menu';

@Component({
  selector: 'kpn-action-menu-item',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <li nz-menu-item (click)="clicked($event)">
      <ng-content />
    </li>
  `,
  imports: [NzMenuItemComponent],
})
export class ActionMenuItemComponent {
  action = output<void>();

  clicked(event: MouseEvent): void {
    event.stopPropagation();
    this.action.emit();
  }
}
