import { NgClass } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';

@Component({
  selector: 'kpn-list-item',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="item" [ngClass]="{ selected: selected() }">
      <ng-content />
    </div>
  `,
  styles: `
    .item {
      border-bottom-color: lightgray;
      border-bottom-style: solid;
      border-bottom-width: 1px;
      padding: 10px;
    }
 
    .item:not(.selected):hover {
      background-color: #f0f0f0;
      cursor: pointer;
    }

    .selected {
      background-color: #e0e0e0;
    }
  `,
  standalone: true,
  imports: [NgClass],
})
export class ListItemComponent {
  selected = input.required<boolean>();
}
