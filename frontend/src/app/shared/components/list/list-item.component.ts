import { NgClass } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatRipple } from '@angular/material/core';

@Component({
  selector: 'ui-list-item',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (clickacle()) {
      <div
        class="kpn-list-item"
        [ngClass]="{ 'kpn-list-item-selected': selected() }"
        matRipple
        matRippleColor="lightgray"
      >
        <ng-content />
      </div>
    } @else {
      <div class="kpn-list-item-base">
        <ng-content />
      </div>
    }
  `,
  imports: [NgClass, MatRipple],
})
export class ListItemComponent {
  readonly clickacle = input<boolean>(false);
  readonly selected = input<boolean>(false);
}
