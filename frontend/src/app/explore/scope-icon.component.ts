import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteScope } from '@api/common/route-scope';
import { NzTooltipDirective } from 'ng-zorro-antd/tooltip';
import { ExploreStyleStandard } from '@app/mapold/style/explore-style-standard';

@Component({
  selector: 'ui-scope-icon',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div
      nz-tooltip
      [nzTooltipTitle]="tooltip()"
      [class]="indicatorClass()"
      [style]="indicatorStyle()"
    ></div>
  `,
  styles: `
    /* referenced in indicatorClass below */
    .indicator-icon {
      border-radius: 50%;
      height: 10px;
      width: 10px;
    }
  `,
  imports: [NzTooltipDirective],
})
export class ScopeIconComponent {
  readonly scope = input.required<RouteScope | string>();
  readonly color = computed(() => this.scopeColor(this.scope()));
  readonly tooltip = computed(() => this.scope() + ' route');
  protected readonly indicatorClass = computed(() => 'indicator-icon ' + this.color());
  protected readonly indicatorStyle = computed(() => 'background-color: ' + this.color());

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
