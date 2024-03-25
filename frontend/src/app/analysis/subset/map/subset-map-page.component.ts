import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { ErrorComponent } from '@app/components/shared/error';
import { PageComponent } from '@app/components/shared/page';
import { RouterService } from '../../../shared/services/router.service';
import { SubsetPageHeaderBlockComponent } from '../components/subset-page-header-block.component';
import { SubsetSidebarComponent } from '../subset-sidebar.component';
import { SubsetMapComponent } from './components/subset-map.component';
import { SubsetMapPageService } from './subset-map-page.service';
import { SubsetMapService } from './subset-map.service';

@Component({
  selector: 'kpn-subset-map-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page [showFooter]="false">
      <kpn-subset-page-header-block
        pageName="map"
        pageTitle="Map"
        i18n-pageTitle="@@subset-map.title"
      />

      <kpn-error />

      @if (service.response(); as response) {
        <kpn-subset-map />
      }
      <kpn-subset-sidebar sidebar />
    </kpn-page>
  `,
  providers: [SubsetMapService, SubsetMapPageService, RouterService],
  standalone: true,
  imports: [
    ErrorComponent,
    PageComponent,
    SubsetMapComponent,
    SubsetPageHeaderBlockComponent,
    SubsetSidebarComponent,
  ],
})
export class SubsetMapPageComponent implements OnInit, OnDestroy {
  protected readonly service = inject(SubsetMapPageService);

  ngOnInit(): void {
    this.service.onInit();
  }

  ngOnDestroy(): void {
    this.service.onDestroy();
  }
}
