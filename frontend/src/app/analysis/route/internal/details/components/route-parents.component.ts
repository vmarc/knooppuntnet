import { NgClass } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { ParentRoute } from '@api/common/route/parent-route';
import { MarkdownModule } from 'ngx-markdown';

@Component({
  selector: 'ui-route-parents',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @for (parentRoute of parentRoutes(); track parentRoute.routeId) {
      <div [ngClass]="'level-' + parentRoute.level">
        <a [routerLink]="'/analysis/route/' + parentRoute.routeId">{{ parentRoute.name }}</a>
      </div>
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
  imports: [MarkdownModule, MatIconModule, RouterLink, NgClass],
})
export class RouteParentsComponent {
  readonly parentRoutes = input.required<Array<ParentRoute>>();
}
