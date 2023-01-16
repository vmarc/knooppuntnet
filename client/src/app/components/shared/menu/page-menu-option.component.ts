import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'kpn-page-menu-option',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a [routerLink]="link" [ngClass]="{ active: active }" class="link">
      <ng-content/>
      <span *ngIf="!!elementCount" class="element-count">
        ({{ elementCount }})
      </span>
    </a>
  `,
  styles: [
    `
      .link {
        white-space: nowrap;
      }

      .active {
        font-weight: bold;
        color: black;
      }

      .element-count {
        color: gray;
        font-weight: normal;
      }
    `,
  ],
})
export class PageMenuOptionComponent {
  @Input() link: string;
  @Input() active = false;
  @Input() state: { [k: string]: any };
  @Input() elementCount: number;
}
