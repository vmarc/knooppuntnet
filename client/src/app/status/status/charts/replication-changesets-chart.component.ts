import {ChangeDetectionStrategy} from '@angular/core';
import {Input} from '@angular/core';
import {Component} from '@angular/core';
import {BarChart} from '../../../kpn/api/common/status/bar-chart';

/* tslint:disable:template-i18n English only */
@Component({
  selector: 'kpn-replication-changesets-chart',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <h2>
      Replication changeset count
    </h2>
    <div class="chart">
      <kpn-action-bar-chart
        [barChart]="barChart"
        [xAxisLabel]="xAxisLabel"
        yAxisLabel="Changesets">
      </kpn-action-bar-chart>
    </div>
  `
})
export class ReplicationChangesetsChartComponent {
  @Input() barChart: BarChart;
  @Input() xAxisLabel: string;
}
