import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { PageComponent } from '@app/components/shared/page';
import { PageHeaderComponent } from '@app/components/shared/page';
import { BaseSidebarComponent } from '@app/shared/base';
import { PoiMapComponent } from './components/poi-map.component';
import { PoiAreasPageService } from './poi-areas-page.service';

@Component({
  selector: 'kpn-poi-areas-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page>
      <kpn-page-header>
        <span i18n="@@poi-areas.title">Point of interest areas</span>
      </kpn-page-header>
      <p i18n="@@poi-areas.comment">
        This map shows the bounding boxes that are used to determine where to collect the point of
        interest information.
      </p>

      @if (service.response(); as response) {
        <kpn-poi-map />
      }
      <kpn-base-sidebar sidebar />
    </kpn-page>
  `,
  providers: [PoiAreasPageService],
  standalone: true,
  imports: [BaseSidebarComponent, PageComponent, PageHeaderComponent, PoiMapComponent],
})
export class PoiAreasPageComponent implements OnInit {
  protected readonly service = inject(PoiAreasPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
