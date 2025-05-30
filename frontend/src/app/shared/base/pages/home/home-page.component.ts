import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { IconButtonComponent } from '@app/shared/components/icon/icon-button.component';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { PageComponent } from '../../../components/page/page.component';

@Component({
  selector: 'ui-home-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-page-header [pageTitle]="null" subject="home" i18n="@@home.page-title"
        >Node networks
      </ui-page-header>
      <ui-icon-button routerLink="/map" icon="map" title="Map" i18n-title="@@home.map" />
      <ui-icon-button
        routerLink="/analysis"
        icon="analysis"
        title="Analysis"
        i18n-title="@@home.analysis"
      />
      <ui-icon-button
        routerLink="/monitor"
        icon="monitor"
        title="Monitor"
        i18n-title="@@home.monitor"
      />
    </ui-page>
  `,
  imports: [IconButtonComponent, PageComponent, PageHeaderComponent, RouterLink],
})
export class HomePageComponent {}
