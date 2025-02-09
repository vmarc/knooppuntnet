import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Reference } from '@api/common/common/reference';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { RouteScopeNameComponent } from '../route-scope-name.component';

@Component({
  selector: 'kpn-icon-link',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-line">
      <nz-icon [nzType]="reference().routeType" />
      <a [routerLink]="link" [state]="state" title="">{{ reference().name }}</a>
      @if (mixedRouteScopes()) {
        <span class="kpn-brackets kpn-thin">
          <kpn-route-scope-name [routeScope]="reference().routeScope" />
        </span>
      }
    </div>
  `,
  imports: [RouteScopeNameComponent, RouterLink, NzIconDirective],
})
export class IconLinkComponent implements OnInit {
  reference = input.required<Reference>();
  mixedRouteScopes = input.required<boolean>();
  elementType = input.required<string>();

  protected state = {};
  protected link: string;

  ngOnInit() {
    this.link = `/analysis/${this.elementType()}/${this.reference().id}`;
    this.state['routeType'] = this.reference().routeType;
    this.state[this.elementType() + 'Name'] = this.reference().name;
  }
}
