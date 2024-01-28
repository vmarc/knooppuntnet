import { NgClass } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'kpn-page-menu-option',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a [routerLink]="link()" [ngClass]="{ active: active() }" [state]="state()" class="link()">
      <ng-content />
      @if (!!elementCount()) {
        <span class="element-count"> ({{ elementCount() }}) </span>
      }
    </a>
  `,
  styles: `
    .link {
      white-space: nowrap;
    }

    .active {
      font-weight: bold;
      color: black;
    }

    .element-count {
      color: grey;
      font-weight: normal;
    }
  `,
  standalone: true,
  imports: [RouterLink, NgClass],
})
export class PageMenuOptionComponent {
  link = input.required<string>();
  active = input(false);
  state = input<
    | {
        [k: string]: any;
      }
    | undefined
  >();
  elementCount = input<number | undefined>();
}
