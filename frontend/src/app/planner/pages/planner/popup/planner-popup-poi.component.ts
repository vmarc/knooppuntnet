import { effect } from '@angular/core';
import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PoiPage } from '@api/common';
import { ApiResponse } from '@api/custom';
import { PoiAnalysisComponent } from '@app/components/poi';
import { InterpretedTags } from '@app/components/shared/tags';
import { TagsTableComponent } from '@app/components/shared/tags';
import { ApiService } from '@app/services';
import { Coordinate } from 'ol/coordinate';
import { ActionButtonNodeComponent } from '../../../../analysis/components/action/action-button-node.component';
import { ActionButtonRelationComponent } from '../../../../analysis/components/action/action-button-relation.component';
import { ActionButtonWayComponent } from '../../../../analysis/components/action/action-button-way.component';
import { PlannerPopupService } from '../../../domain/context/planner-popup-service';
import { PlannerService } from '../planner.service';
import { MapService } from '../map.service';

@Component({
  selector: 'kpn-planner-popup-poi',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (response(); as response) {
      @if (!response.result) {
        <div class="item" i18n="@@poi.detail.none">No details available</div>
      } @else {
        @if (response.result.analysis; as poi) {
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
            @if (poiClick().poiId.elementType === 'node') {
              <kpn-action-button-node [nodeId]="poiClick().poiId.elementId" />
            } @else if (poiClick().poiId.elementType === 'way') {
              <kpn-action-button-way [wayId]="poiClick().poiId.elementId" />
            } @else if (poiClick().poiId.elementType === 'relation') {
              <kpn-action-button-relation [relationId]="poiClick().poiId.elementId" />
            }
          </div>
        }
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
    PoiAnalysisComponent,
    TagsTableComponent,
  ],
})
export class PlannerPopupPoiComponent {
  private readonly service = inject(PlannerPopupService);
  private readonly apiService = inject(ApiService);
  private readonly plannerService = inject(PlannerService);
  private readonly mapService = inject(MapService);
  protected readonly poiClick = this.service.poiClick;

  protected response = signal<ApiResponse<PoiPage>>(null);

  constructor() {
    effect(() => {
      const poiClick = this.poiClick();
      if (poiClick !== null) {
        this.plannerService.context.cursor.setStyleWait();
        this.apiService
          .poi(poiClick.poiId.elementType, poiClick.poiId.elementId)
          .subscribe((response) => {
            this.response.set(response);
            this.openPopup(poiClick.coordinate);
            this.plannerService.context.cursor.setStyleDefault();
            this.plannerService.context.highlighter.reset();
          });
      }
    });
  }

  mainTags(): InterpretedTags {
    return InterpretedTags.all(this.response().result.analysis.mainTags);
  }

  extraTags(): InterpretedTags {
    return InterpretedTags.all(this.response().result.analysis.extraTags);
  }

  private openPopup(coordinate: Coordinate): void {
    setTimeout(() => this.plannerService.context.plannerPopup.setPosition(coordinate, -45), 0);
  }
}
