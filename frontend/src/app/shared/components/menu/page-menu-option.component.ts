import { NgClass } from '@angular/common';
import { effect } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { IntegerFormatPipe } from '@app/shared/components/format/integer-format.pipe';

@Component({
  selector: 'ui-page-menu-option',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a [routerLink]="link()" [ngClass]="{ active: active() }" [state]="state()" class="link">
      <ng-content />
      @if (!!elementCount()) {
        <span class="element-count"> ({{ elementCount() | integer }}) </span>
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
  imports: [RouterLink, NgClass, IntegerFormatPipe, IntegerFormatPipe],
})
export class PageMenuOptionComponent {
  readonly link = input.required<string>();
  readonly active = input(false);
  readonly state = input<
    | {
        [k: string]: any;
      }
    | undefined
  >();
  readonly elementCount = input<number | undefined>();

  constructor() {
    effect(() => {
      console.log('menu-option=' + this.link() + ', active=' + this.active());
    });
  }
}
