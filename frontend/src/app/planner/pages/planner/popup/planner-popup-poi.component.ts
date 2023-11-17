import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { PoiAnalysis } from '@api/common';
import { PoiPage } from '@api/common';
import { ApiResponse } from '@api/custom';
import { Tags } from '@api/custom';
import { PoiAnalysisComponent } from '@app/components/poi';
import { JosmLinkComponent } from '@app/components/shared/link';
import { OsmLinkComponent } from '@app/components/shared/link';
import { InterpretedTags } from '@app/components/shared/tags';
import { TagsTableComponent } from '@app/components/shared/tags';
import { ApiService } from '@app/services';
import { PoiService } from '@app/services';
import { Coordinate } from 'ol/coordinate';
import { Observable } from 'rxjs';
import { filter, mergeMap, tap } from 'rxjs/operators';
import { PoiClick } from '../../../domain/interaction/actions/poi-click';
import { PlannerService } from '../../../planner.service';
import { MapService } from '../../../services/map.service';

@Component({
  selector: 'kpn-planner-popup-poi',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="response$ | async">
      <div *ngIf="!poi" class="item" i18n="@@poi.detail.none">
        No details available
      </div>

      <div *ngIf="poi">
        <kpn-poi-analysis [poi]="poi" />

        <div *ngIf="poi.mainTags && poi.mainTags.tags.length > 0" class="item">
          <kpn-tags-table [tags]="mainTags()" />
        </div>

        <div
          *ngIf="poi.extraTags && poi.extraTags.tags.length > 0"
          class="item"
        >
          <kpn-tags-table [tags]="extraTags()" />
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
    </div>
  `,
  styles: `
    .item {
      margin-top: 10px;
      margin-bottom: 10px;
    }

    .item * {
      margin-right: 10px;
      align-items: center;
    }
  `,
  standalone: true,
  imports: [
    AsyncPipe,
    JosmLinkComponent,
    NgIf,
    OsmLinkComponent,
    PoiAnalysisComponent,
    TagsTableComponent,
  ],
})
export class PlannerPopupPoiComponent implements OnInit {
  response$: Observable<ApiResponse<PoiPage>>;

  poiClick: PoiClick;
  poiPage: PoiPage;
  poi: PoiAnalysis;
  tags: Tags;

  constructor(
    private mapService: MapService,
    private apiService: ApiService,
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
        this.apiService.poi(
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
