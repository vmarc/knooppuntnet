import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'ui-link-fact',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a [routerLink]="'/analysis/' + fact() + '/' + country() + '/' + routeType()">{{ fact() }}</a>
  `,
  imports: [RouterLink],
})
export class LinkFactComponent {
  readonly fact = input.required<string>();
  readonly country = input.required<string>();
  readonly routeType = input.required<string>();
}
