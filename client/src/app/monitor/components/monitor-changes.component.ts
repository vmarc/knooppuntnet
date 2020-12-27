import {Input} from '@angular/core';
import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {MonitorRouteChangeSummary} from '@api/common/monitor/monitor-route-change-summary';

@Component({
  selector: 'kpn-monitor-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <kpn-items>
      <kpn-item *ngFor="let changeSet of changes; let i=index" [index]="rowIndex(i)">

        <div class="change-set">

          <kpn-monitor-change-header [changeSet]="changeSet"></kpn-monitor-change-header>

          <div>
            <p>
              Reference: {{changeSet.gpxFilename}}
            </p>
            <table>
              <tr>
                <td>GPX</td>
                <td>{{changeSet.gpxDistance}}km</td>
              </tr>
              <tr>
                <td>OSM</td>
                <td>{{changeSet.osmDistance}}km</td>
              </tr>
            </table>
            <p>
              ways={{changeSet.wayCount}},
              added={{changeSet.waysAdded}},
              removed={{changeSet.waysRemoved}},
              updated={{changeSet.waysUpdated}}
            </p>
            <p *ngIf="changeSet.routeSegmentCount !== 1">
              Not OK: {{changeSet.routeSegmentCount}} route segments
            </p>
            <p *ngIf="changeSet.routeSegmentCount === 1">
              OK: 1 route segment
            </p>
            <p *ngIf="changeSet.resolvedNokSegmentCount > 0" class="kpn-line">
              <span>Resolved deviations: {{changeSet.resolvedNokSegmentCount}}</span>
              <kpn-icon-happy></kpn-icon-happy>
            </p>
            <p *ngIf="changeSet.newNokSegmentCount > 0" class="kpn-line">
              <span>New deviations: {{changeSet.newNokSegmentCount}}</span>
              <kpn-icon-investigate></kpn-icon-investigate>
            </p>
          </div>
        </div>
      </kpn-item>
    </kpn-items>
  `,
  styles: [`
    .change-set {
      margin-top: 5px;
      margin-bottom: 5px;
    }
  `]
})
export class MonitorChangesComponent {

  @Input() pageIndex: number;
  @Input() itemsPerPage: number;
  @Input() changes: MonitorRouteChangeSummary[];

  rowIndex(index: number): number {
    return this.pageIndex * this.itemsPerPage + index;
  }
}
