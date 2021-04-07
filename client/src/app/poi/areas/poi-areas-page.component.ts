import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiResponse } from '@api/custom/api-response';
import { AppService } from '../../app.service';
import { PageService } from '../../components/shared/page.service';

@Component({
  selector: 'kpn-poi-areas-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page-header>
      <span i18n="@@poi-areas.title">Point of interest areas</span>
    </kpn-page-header>
    <p i18n="@@poi-areas.comment">
      This map shows the bounding boxes that are used to determine where to
      collect the point of interest information.
    </p>

    <div *ngIf="response$ | async as response">
      <kpn-poi-map [geoJson]="response.result"></kpn-poi-map>
    </div>
  `,
})
export class PoiAreasPageComponent implements OnInit, OnDestroy {
  response$: Observable<ApiResponse<string>>;

  constructor(
    private appService: AppService,
    private pageService: PageService
  ) {
    this.pageService.showFooter = false;
  }

  ngOnInit(): void {
    this.response$ = this.appService.poiAreas();
  }

  ngOnDestroy(): void {
    this.pageService.showFooter = true;
  }
}
