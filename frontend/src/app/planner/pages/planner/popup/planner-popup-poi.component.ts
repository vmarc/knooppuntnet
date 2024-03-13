import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { PoiAnalysis } from '@api/common';
import { PoiPage } from '@api/common';
import { ApiResponse } from '@api/custom';
import { Tags } from '@api/custom';
import { PoiAnalysisComponent } from '@app/components/poi';
import { InterpretedTags } from '@app/components/shared/tags';
import { TagsTableComponent } from '@app/components/shared/tags';
import { ApiService } from '@app/services';
import { Coordinate } from 'ol/coordinate';
import { Observable } from 'rxjs';
import { filter, mergeMap, tap } from 'rxjs/operators';
import { ActionButtonNodeComponent } from '../../../../analysis/components/action/action-button-node.component';
import { ActionButtonRelationComponent } from '../../../../analysis/components/action/action-button-relation.component';
import { ActionButtonWayComponent } from '../../../../analysis/components/action/action-button-way.component';
import { PoiClick } from '../../../domain/interaction/actions/poi-click';
import { PlannerService } from '../../../planner.service';
import { MapService } from '../../../services/map.service';

@Component({
  selector: 'kpn-planner-popup-poi',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (response$ | async) {
      @if (!poi) {
        <div class="item" i18n="@@poi.detail.none">No details available</div>
      } @else {
        <div>
          <kpn-poi-analysis [poi]="poi" />
          @if (poi.mainTags && poi.mainTags.tags.length > 0) {
            <div class="item">
              <kpn-tags-table [tags]="mainTags()" />
            </div>
          }
          @if (poi.extraTags && poi.extraTags.tags.length > 0) {
            <div class="item">
              <kpn-tags-table [tags]="extraTags()" />
            </div>
          }
          @if (poiClick.poiId.elementType === 'node') {
            <kpn-action-button-node [nodeId]="poiClick.poiId.elementId" />
          } @else if (poiClick.poiId.elementType === 'way') {
            <kpn-action-button-way [wayId]="poiClick.poiId.elementId" />
          } @else if (poiClick.poiId.elementType === 'relation') {
            <kpn-action-button-relation [relationId]="poiClick.poiId.elementId" />
          }
        </div>
      }
    }
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
    ActionButtonNodeComponent,
    ActionButtonRelationComponent,
    ActionButtonWayComponent,
    AsyncPipe,
    PoiAnalysisComponent,
    TagsTableComponent,
  ],
})
export class PlannerPopupPoiComponent implements OnInit {
  private readonly mapService = inject(MapService);
  private readonly apiService = inject(ApiService);
  private readonly plannerService = inject(PlannerService);

  protected response$: Observable<ApiResponse<PoiPage>>;

  protected poiClick: PoiClick;
  protected poiPage: PoiPage;
  protected poi: PoiAnalysis;
  protected tags: Tags;

  ngOnInit(): void {
    this.response$ = this.mapService.poiClicked$.pipe(
      filter((poiClick) => poiClick !== null),
      tap((poiClick) => {
        this.plannerService.context.cursor.setStyleWait();
        this.poiClick = poiClick;
      }),
      mergeMap((poiClick) =>
        this.apiService.poi(poiClick.poiId.elementType, poiClick.poiId.elementId)
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
    setTimeout(() => this.plannerService.context.overlay.setPosition(coordinate, -45), 0);
  }
}
