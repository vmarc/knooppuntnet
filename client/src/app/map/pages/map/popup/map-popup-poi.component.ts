import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { PoiAnalysis } from '@api/common/poi-analysis';
import { PoiPage } from '@api/common/poi-page';
import { ApiResponse } from '@api/custom/api-response';
import { Tags } from '@api/custom/tags';
import { Coordinate } from 'ol/coordinate';
import { Observable } from 'rxjs';
import { filter, mergeMap, tap } from 'rxjs/operators';
import { AppService } from '../../../../app.service';
import { PoiClick } from '../../../../components/ol/domain/poi-click';
import { MapService } from '../../../../components/ol/services/map.service';
import { InterpretedTags } from '../../../../components/shared/tags/interpreted-tags';
import { PoiService } from '../../../../services/poi.service';
import { PlannerService } from '../../../planner.service';

@Component({
  selector: 'kpn-map-popup-poi',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="response$ | async">
      <div *ngIf="!poi" class="item" i18n="@@poi.detail.none">
        No details available
      </div>

      <div *ngIf="poi">
        <kpn-poi-analysis [poi]="poi"/>
      </div>

      <div *ngIf="poi.mainTags && poi.mainTags.tags.length > 0" class="item">
        <kpn-tags-table [tags]="mainTags()"/>
      </div>

      <div *ngIf="poi.extraTags && poi.extraTags.tags.length > 0" class="item">
        <kpn-tags-table [tags]="extraTags()"/>
      </div>

      <div class="item">
        <kpn-osm-link
          [kind]="poiClick.poiId.elementType"
          [elementId]="poiClick.poiId.elementId.toString()"
          title="osm"
        />
        <kpn-josm-link
          [kind]="poiClick.poiId.elementType"
          [elementId]="poiClick.poiId.elementId"
          title="edit"
        />
      </div>
    </div>
  `,
  styles: [
    `
      .item {
        margin-top: 10px;
        margin-bottom: 10px;
      }

      .item * {
        margin-right: 10px;
        align-items: center;
      }
    `,
  ],
})
export class MapPopupPoiComponent implements OnInit {
  response$: Observable<ApiResponse<PoiPage>>;

  poiClick: PoiClick;
  poiPage: PoiPage;
  poi: PoiAnalysis;
  tags: Tags;

  constructor(
    private mapService: MapService,
    private appService: AppService,
    private poiService: PoiService,
    private plannerService: PlannerService
  ) {}

  ngOnInit(): void {
    this.response$ = this.mapService.poiClicked$.pipe(
      filter((poiClick) => poiClick !== null),
      tap((poiClick) => {
        this.plannerService.context.cursor.setStyleWait();
        this.poiClick = poiClick;
      }),
      mergeMap((poiClick) =>
        this.appService.poi(
          poiClick.poiId.elementType,
          poiClick.poiId.elementId
        )
      ),
      tap((response) => {
        if (response.result) {
          this.poiPage = response.result;
          this.poi = response.result.analysis;
          this.tags = response.result.analysis.mainTags;
        } else {
          this.poiPage = null;
          this.poi = null;
          this.tags = null;
        }
        this.openPopup(this.poiClick.coordinate);
        this.plannerService.context.cursor.setStyleDefault();
        this.plannerService.context.highlighter.reset();
      })
    );
  }

  mainTags(): InterpretedTags {
    return InterpretedTags.all(this.poi.mainTags);
  }

  extraTags(): InterpretedTags {
    return InterpretedTags.all(this.poi.extraTags);
  }

  private openPopup(coordinate: Coordinate): void {
    setTimeout(
      () => this.plannerService.context.overlay.setPosition(coordinate, -45),
      0
    );
  }
}
