import { NgClass } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatRipple } from '@angular/material/core';

@Component({
  selector: 'ui-list-item',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div
      class="kpn-list-item"
      [ngClass]="{ 'kpn-list-item-selected': selected() }"
      matRipple
      matRippleColor="lightgray"
    >
      <ng-content />
    </div>
  `,
  imports: [NgClass, MatRipple],
})
export class ListItemComponent {
  selected = input.required<boolean>();
}
