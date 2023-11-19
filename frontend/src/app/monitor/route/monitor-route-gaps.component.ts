import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteGapComponent } from './monitor-route-gap.component';

@Component({
  selector: 'kpn-monitor-route-gaps',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <div class="page">
      <h1>Monitor subrelation gap indicators</h1>
      <table class="kpn-table">
        <tr *ngFor="let description of gapDescriptions" class="row">
          <td>
            {{ description }}
          </td>
          <td class="gap">
            <kpn-monitor-route-gap [description]="description" [osmSegmentCount]="99"/>
          </td>
        </tr>
      </table>
    </div>
  `,
  styles: `
    .page {
      padding: 1em;
      width: 100%;
    }

    .row {
      height: 2.5rem;
    }

    .gap {
      padding: 0;
    }
  `,
  standalone: true,
  imports: [NgFor, MonitorRouteGapComponent],
})
export class MonitorRouteGapsComponent {
  readonly gapDescriptions = [
    '',
    '',
    'top-middle-bottom',
    'top',
    'middle',
    'bottom',
    'top-middle',
    'middle-bottom',
    'start',
    'start-middle',
    'start-middle-bottom',
    'end',
    'top-end',
    'middle-end',
    'top-middle-end',
  ];
}
