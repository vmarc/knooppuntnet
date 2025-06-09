import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { RouteStructureRow } from '@api/common/route/route-structure-row';

@Component({
  selector: 'ui-route-member-name',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let row = structureRow();
    @if (row.poi) {
      <span class="way-type">{{ row.poi }}</span>
    }
    @if (row.way) {
      @if (row.way.wayType) {
        <span class="way-type">{{ row.way.wayType }}</span>
      }
    }
    @if (row.name) {
      <span class="name">
        {{ row.name }}
      </span>
    }
    @if (row.relation) {
      @switch (row.relation.level) {
        @case (1) {
          <div class="level-1 name">
            <a [routerLink]="'/analysis/route/' + row.id">{{ row.relation.name }}</a>
          </div>
        }
        @case (2) {
          <div class="level-2 name">
            <a [routerLink]="'/analysis/route/' + row.id">{{ row.relation.name }}</a>
          </div>
        }
        @case (3) {
          <div class="level-3 name">
            <a [routerLink]="'/analysis/route/' + row.id">{{ row.relation.name }}</a>
          </div>
        }
        @case (4) {
          <div class="level-4 name">
            <a [routerLink]="'/analysis/route/' + row.id">{{ row.relation.name }}</a>
          </div>
        }
        @case (5) {
          <div class="level-5 name">
            <a [routerLink]="'/analysis/route/' + row.id">{{ row.relation.name }}</a>
          </div>
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

    .way-type {
      font-style: italic;
      padding: 0.3em 0.8em 0.3em 0.5em;
      margin-right: 1em;
      border-radius: 1em;
      border: 1px solid lightgray;
    }

    .name {
      margin-right: 2em;
    }
  `,
  imports: [MatIconModule, RouterLink],
})
export class RouteMemberNameComponent {
  readonly structureRow = input.required<RouteStructureRow>();
}
