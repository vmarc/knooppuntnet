import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-longdistance-routes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li>Routes</li>
    </ul>

    <h1>Long distance routes</h1>

    <kpn-longdistance-routes-table></kpn-longdistance-routes-table>
  `,
})
export class LongdistanceRoutesComponent {}
