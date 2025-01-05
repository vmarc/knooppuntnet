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
      @if (r.way.wayType) {
        <span class="way-type">{{ r.way.wayType }}</span>
      }
      @if (r.way.description) {
        <span class="name">
          {{ r.way.description }}
        </span>
      }
    }
    @if (r.relation) {
      {{ r.relation.level }}
      @switch (r.relation.level) {
        @case (1) {
          <div class="level-1 name">{{ r.relation.name }}</div>
        }
        @case (2) {
          <div class="level-2 name">{{ r.relation.name }}</div>
        }
        @case (3) {
          <div class="level-3 name">{{ r.relation.name }}</div>
        }
        @case (4) {
          <div class="level-4 name">{{ r.relation.name }}</div>
        }
        @case (5) {
          <div class="level-5 name">{{ r.relation.name }}</div>
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
  imports: [MatIconModule],
})
export class RouteMemberNameComponent {
  row = input.required<RouteStructureRow>();
}
