import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { RouteType } from '@api/common/route-type';

@Component({
  selector: 'ui-link-route',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a
      [routerLink]="routeLink()"
      [state]="{ routeType: routeType(), routeName: routeName() }"
      title="Open route page"
      i18n-title="@@link-route.title"
      >{{ linkTitle() }}</a
    >
  `,
  imports: [RouterLink],
})
export class LinkRouteComponent {
  readonly routeId = input.required<number>();
  readonly routeName = input.required<string>();
  readonly routeType = input<RouteType>();
  readonly title = input<string>();
  protected readonly routeLink = computed(() => `/analysis/route/${this.routeId()}`);
  protected readonly linkTitle = computed(() => (this.title() ? this.title()! : this.routeName()));
}
