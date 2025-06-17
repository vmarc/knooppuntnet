import { NgTemplateOutlet } from '@angular/common';
import { NgClass } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatRipple } from '@angular/material/core';

@Component({
  selector: 'ui-list-item',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (clickable()) {
      <div
        class="kpn-list-item"
        [ngClass]="{ 'kpn-list-item-selected': selected() }"
        matRipple
        matRippleColor="lightgray"
      >
        <ng-container *ngTemplateOutlet="item" />
      </div>
    } @else {
      <div class="kpn-list-item-base">
        <ng-container *ngTemplateOutlet="item" />
      </div>
    }
    <ng-template #item>
      <ng-content />
    </ng-template>
  `,
  imports: [NgClass, MatRipple, NgTemplateOutlet],
})
export class ListItemComponent {
  readonly clickable = input<boolean>(false);
  readonly selected = input<boolean>(false);
}
