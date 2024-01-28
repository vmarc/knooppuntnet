import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NetworkType } from '@api/custom';

@Component({
  selector: 'kpn-link-route',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a
      [routerLink]="'/analysis/route/' + routeId()"
      [state]="{ networkType: networkType(), routeName: routeName() }"
      title="Open route page"
      i18n-title="@@link-route.title"
      >{{ linkTitle }}</a
    >
  `,
  standalone: true,
  imports: [RouterLink],
})
export class LinkRouteComponent implements OnInit {
  routeId = input.required<number>();
  routeName = input.required<string>();
  networkType = input<NetworkType | undefined>();
  title = input<string | undefined>();

  protected linkTitle = '';

  ngOnInit(): void {
    this.linkTitle = this.title() ? this.title()! : this.routeName();
  }
}
