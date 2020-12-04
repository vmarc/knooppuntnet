import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';

@Component({
  selector: 'kpn-long-distance-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li>Long distance routes</li>
    </ul>

    <h1>
      Long distance routes
    </h1>

    <kpn-long-distance-routes-table></kpn-long-distance-routes-table>

  `,
  styles: [`
  `]
})
export class LongDistanceRoutesComponent {
}
