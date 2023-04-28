import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { NameValue } from '@api/common/status';

@Component({
  selector: 'kpn-server-disk-usage-pie-chart',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="server">
      <span>{{ title }}</span>
      <ngx-charts-pie-chart
        [animations]="false"
        [customColors]="customColors"
        [margins]="[0, 0, 0, 0]"
        [view]="view"
        [results]="data"
      />
      <span>{{ total }}</span>
    </div>
  `,
  styles: [
    `
      .server {
        display: flex;
        flex-direction: column;
        align-items: center;
        margin-bottom: 30px;
      }

      ngx-charts-pie-chart {
        margin: 15px 15px 5px;
      }
    `,
  ],
})
export class ServerDiskUsagePieChartComponent {
  @Input() title: string;
  @Input() data: NameValue[];
  @Input() total: string;

  view: [number, number] = [100, 100];

  customColors = [
    {
      name: 'Overpass',
      value: '#346beb',
    },
    {
      name: 'Used',
      value: '#34b1eb',
    },
    {
      name: 'Free',
      value: '#bdeb34',
    },
  ];
}
