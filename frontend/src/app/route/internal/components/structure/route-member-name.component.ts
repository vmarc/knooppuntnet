import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { StructureRow } from '@api/common/route/structure-row';

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
      <div>
        <a [routerLink]="link()">{{ row.relation.name }}</a>
      </div>
    }
  `,
  styles: `
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
  readonly structureRow = input.required<StructureRow>();
  protected readonly link = computed(() => '/analysis/route/' + this.structureRow().id);
}
