import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { PageHeaderComponent } from '@app/components/shared/page';
import { FrisoMapComponent } from './friso-map.component';

@Component({
  selector: 'kpn-friso-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- For now, English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
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
  `,
  standalone: true,
  imports: [RouterLink, PageHeaderComponent, MatIconModule, FrisoMapComponent],
})
export class FrisoPageComponent {}
