import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { PageComponent } from '@app/components/shared/page';
import { PageHeaderComponent } from '@app/components/shared/page';
import { FrisoMapComponent } from './friso-map.component';
import { FrisoSidebarComponent } from './friso-sidebar.component';

@Component({
  selector: 'kpn-friso-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- For now, English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <kpn-page>
      <ul class="breadcrumb">
        <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
        <li><span>Routedatabank</span></li>
      </ul>

      <kpn-page-header pageTitle="Routedatabank">
        <span class="header-network-type-icon">
          <mat-icon svgIcon="hiking" />
        </span>
        <span> Netherlands: OSM versus routedatabank comparison </span>
      </kpn-page-header>

      <kpn-friso-map />
      <kpn-friso-sidebar sidebar />
    </kpn-page>
  `,
  standalone: true,
  imports: [
    FrisoMapComponent,
    FrisoSidebarComponent,
    MatIconModule,
    PageComponent,
    PageHeaderComponent,
    RouterLink,
  ],
})
export class FrisoPageComponent {}
