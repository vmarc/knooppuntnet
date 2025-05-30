import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { PageHeaderComponent } from '@app/shared/components/page/page-header.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { PoiMapComponent } from './components/poi-map.component';
import { PoiAreasPageService } from './poi-areas-page.service';

@Component({
  selector: 'ui-poi-areas-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-page-header>
        <span i18n="@@poi-areas.title">Point of interest areas</span>
      </ui-page-header>
      <p i18n="@@poi-areas.comment">
        This map shows the bounding boxes that are used to determine where to collect the point of
        interest information.
      </p>

      @if (service.response(); as response) {
        <ui-poi-map />
      }
    </ui-page>
  `,
  providers: [PoiAreasPageService],
  imports: [PageHeaderComponent, PoiMapComponent, PageComponent],
})
export class PoiAreasPageComponent implements OnInit {
  readonly service = inject(PoiAreasPageService);

  ngOnInit(): void {
    this.service.onInit();
  }
}
