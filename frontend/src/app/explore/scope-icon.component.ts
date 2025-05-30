import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatTooltip } from '@angular/material/tooltip';
import { RouteScope } from '@api/common/route-scope';
import { ExploreStyleStandard } from '../map/style/explore-style-standard';

@Component({
  selector: 'ui-scope-icon',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div
      [matTooltip]="tooltip()"
      [class]="'indicator-icon ' + color()"
      [style]="'background-color: ' + color()"
    ></div>
  `,
  styles: `
    .indicator-icon {
      border-radius: 50%;
      height: 10px;
      width: 10px;
    }
  `,
  imports: [MatTooltip],
})
export class ScopeIconComponent {
  scope = input.required<RouteScope | string>();
  color = computed(() => this.scopeColor(this.scope()));
  tooltip = computed(() => this.scope() + ' route');

  private scopeColor(scope: RouteScope | string): string {
    if (scope === 'international') {
      return ExploreStyleStandard.colorInternational;
    }
    if (scope === 'national') {
      return ExploreStyleStandard.colorNational;
    }
    if (scope === 'regional') {
      return ExploreStyleStandard.colorRegional;
    }
    return 'gray';
  }
}
