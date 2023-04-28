import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'kpn-link-fact',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a [routerLink]="'/analysis/' + fact + '/' + country + '/' + networkType">{{
      fact
    }}</a>
  `,
  standalone: true,
  imports: [RouterLink],
})
export class LinkFactComponent {
  @Input() fact: string;
  @Input() country: string;
  @Input() networkType: string;
}
