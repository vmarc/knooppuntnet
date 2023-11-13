import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { RouterLink } from '@angular/router';
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
  standalone: true,
  imports: [RouterLink],
})
export class LinkRouteComponent implements OnInit {
  @Input({ required: true }) routeId: number;
  @Input({ required: true }) routeName: string;
  @Input({ required: false }) networkType: NetworkType;
  @Input({ required: false }) title: string;

  protected linkTitle = '';

  ngOnInit(): void {
    this.linkTitle = this.title ? this.title : this.routeName;
  }
}
