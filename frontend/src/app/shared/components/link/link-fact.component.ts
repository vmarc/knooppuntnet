import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'kpn-link-fact',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a [routerLink]="'/analysis/' + fact() + '/' + country() + '/' + routeType()">{{ fact() }}</a>
  `,
  imports: [RouterLink],
})
export class LinkFactComponent {
  fact = input.required<string>();
  country = input.required<string>();
  routeType = input.required<string>();
}
