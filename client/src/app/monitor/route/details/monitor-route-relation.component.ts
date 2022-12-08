import { OnInit } from '@angular/core';
import { Input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-monitor-route-relation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="link">
      <kpn-osm-link-relation
        [relationId]="relation.relationId"
        [title]="relation.relationId"
      >
      </kpn-osm-link-relation>
    </div>
    <div class="distance">{{ distance }}</div>
    <div *ngIf="level === 1" class="level-1">{{ name }}</div>
    <div *ngIf="level === 2" class="level-2">{{ name }}</div>
    <div *ngIf="level === 3" class="level-3">{{ name }}</div>
    <div *ngIf="level === 4" class="level-4">{{ name }}</div>
  `,
  styles: [
    `
      .link {
        display: inline-block;
        min-width: 7em;
        height: 2em;
      }

      .distance {
        display: inline-block;
        min-width: 5em;
      }

      .level-1 {
        display: inline-block;
      }

      .level-2 {
        display: inline-block;
        margin-left: 2em;
      }

      .level-3 {
        display: inline-block;
        margin-left: 4em;
      }

      .level-4 {
        display: inline-block;
        margin-left: 6em;
      }
    `,
  ],
})
export class MonitorRouteRelationComponent implements OnInit {
  @Input() level;
  @Input() relation;

  name: string;
  distance: string;

  ngOnInit(): void {
    if (this.relation.name) {
      this.name = this.relation.name;
    } else if (this.relation.from && this.relation.to) {
      this.name = `${this.relation.from} - ${this.relation.to}`;
    } else {
      this.name = '?';
    }

    const meters = this.relation.osmDistance;
    if (meters > 1000) {
      const km = Math.round(meters / 1000);
      this.distance = `${km}km`;
    } else {
      this.distance = `${meters}m`;
    }
  }
}
