import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { NetworkType } from '@api/custom';

@Component({
  selector: 'kpn-link-route',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a
      [routerLink]="'/analysis/route/' + routeId"
      [state]="{networkType, routeName}"
      title="Open route page"
      i18n-title="@@link-route.title"
      >{{ linkTitle }}</a
    >
  `,
})
export class LinkRouteComponent implements OnInit {
  @Input() routeId: number;
  @Input() networkType: NetworkType;
  @Input() routeName: string;
  @Input() title: string;

  protected linkTitle = '';

  ngOnInit(): void {
    this.linkTitle = this.title ? this.title : this.routeName;
  }
}
