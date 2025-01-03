import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouteStructureRow } from '@api/common/route';

@Component({
  selector: 'kpn-route-member-name',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let r = row();
    @if (r.way) {
      {{ r.way.description }}
    }
    @if (r.relation) {
      {{ r.relation.level }}
      @switch (r.relation.level) {
        @case (1) {
          <div class="level-1">{{ r.relation.name }}</div>
        }
        @case (2) {
          <div class="level-2">{{ r.relation.name }}</div>
        }
        @case (3) {
          <div class="level-3">{{ r.relation.name }}</div>
        }
        @case (4) {
          <div class="level-4">{{ r.relation.name }}</div>
        }
        @case (5) {
          <div class="level-5">{{ r.relation.name }}</div>
        }
      }
    }
  `,
  styles: `
    .level-1 {
    }

    .level-2 {
      margin-left: 1.5em;
    }

    .level-3 {
      margin-left: 3em;
    }

    .level-4 {
      margin-left: 4.5em;
    }

    .level-5 {
      margin-left: 6em;
    }
  `,
  imports: [MatIconModule],
})
export class RouteMemberNameComponent {
  row = input.required<RouteStructureRow>();
}
