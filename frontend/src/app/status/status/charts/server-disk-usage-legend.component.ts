import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-server-disk-usage-legend',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <div class="legend">
      <div>
        <div class="colorbox overpass"></div>
        Overpass
      </div>
      <div>
        <div class="colorbox used"></div>
        Used
      </div>
      <div>
        <div class="colorbox free"></div>
        Free
      </div>
    </div>
  `,
  styles: `
    .legend {
      margin-top: 30px;
      margin-left: 20px;
      margin-bottom: 30px;
    }

    .colorbox {
      display: inline-block;
      height: 10px;
      width: 10px;
      margin-right: 5px;
      border: 1px solid black;
    }

    .overpass {
      background-color: #346beb;
    }

    .used {
      background-color: #34b1eb;
    }

    .free {
      background-color: #bdeb34;
    }
  `,
  standalone: true,
})
export class ServerDiskUsageLegendComponent {}
