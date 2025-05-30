import { OnInit } from '@angular/core';
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
      [routerLink]="'/analysis/route/' + routeId()"
      [state]="{ routeType: routeType(), routeName: routeName() }"
      title="Open route page"
      i18n-title="@@link-route.title"
      >{{ linkTitle }}</a
    >
  `,
  imports: [RouterLink],
})
export class LinkRouteComponent implements OnInit {
  routeId = input.required<number>();
  routeName = input.required<string>();
  routeType = input<RouteType>();
  title = input<string>();

  protected linkTitle = '';

  ngOnInit(): void {
    this.linkTitle = this.title() ? this.title()! : this.routeName();
  }
}
