import { Input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteRelation } from '@api/common/monitor/monitor-route-relation';

@Component({
  selector: 'kpn-monitor-route-details-structure',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="relation as relationLevel1">
      <kpn-monitor-route-relation
        [level]="1"
        [relation]="relationLevel1"
      ></kpn-monitor-route-relation>
      <div *ngFor="let relationLevel2 of relationLevel1.relations">
        <kpn-monitor-route-relation
          [level]="2"
          [relation]="relationLevel2"
        ></kpn-monitor-route-relation>
        <div *ngFor="let relationLevel3 of relationLevel2.relations">
          <kpn-monitor-route-relation
            [level]="3"
            [relation]="relationLevel3"
          ></kpn-monitor-route-relation>
        </div>
      </div>
    </div>
  `,
})
export class MonitorRouteDetailsStructureComponent {
  @Input() relation: MonitorRouteRelation;
}
