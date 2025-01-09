import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Reference } from '@api/common/common';
import { NetworkScopeNameComponent } from '../network-scope-name.component';
import { RouteTypeIconComponent } from '../network-type-icon.component';

@Component({
  selector: 'kpn-icon-link',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-line">
      <kpn-network-type-icon [routeType]="reference().routeType" />
      <a [routerLink]="link" [state]="state" title="">{{ reference().name }}</a>
      @if (mixedNetworkScopes()) {
        <span class="kpn-brackets kpn-thin">
          <kpn-network-scope-name [networkScope]="reference().networkScope" />
        </span>
      }
    </div>
  `,
  imports: [NetworkScopeNameComponent, RouteTypeIconComponent, RouterLink],
})
export class IconLinkComponent implements OnInit {
  reference = input.required<Reference>();
  mixedNetworkScopes = input.required<boolean>();
  elementType = input.required<string>();

  protected state = {};
  protected link: string;

  ngOnInit() {
    this.link = `/analysis/${this.elementType()}/${this.reference().id}`;
    this.state['routeType'] = this.reference().routeType;
    this.state[this.elementType() + 'Name'] = this.reference().name;
  }
}
