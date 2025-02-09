import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PoiAnalysisComponent } from '@app/shared/components/poi/poi-analysis.component';
import { InterpretedTags } from '@app/shared/components/tags/interpreted-tags';
import { TagTableComponent } from '@app/shared/components/tags/tag-table.component';
import { ActionButtonNodeComponent } from '@app/analysis/components/action/action-button-node.component';
import { ActionButtonRelationComponent } from '@app/analysis/components/action/action-button-relation.component';
import { ActionButtonWayComponent } from '@app/analysis/components/action/action-button-way.component';
import { PlannerPopupService } from '../../../domain/context/planner-popup-service';

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
            @if (poi.mainTags && poi.mainTags.length > 0) {
              <div class="item">
                <kpn-tag-table [tags]="mainTags()" />
              </div>
            }
            @if (poi.extraTags && poi.extraTags.length > 0) {
              <div class="item">
                <kpn-tag-table [tags]="extraTags()" />
              </div>
            }
            @if (response.result.elementType === 'node') {
              <kpn-action-button-node [nodeId]="response.result.elementId" />
            } @else if (response.result.elementType === 'way') {
              <kpn-action-button-way [wayId]="response.result.elementId" />
            } @else if (response.result.elementType === 'relation') {
              <kpn-action-button-relation [relationId]="response.result.elementId" />
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
  imports: [
    ActionButtonNodeComponent,
    ActionButtonRelationComponent,
    ActionButtonWayComponent,
    PoiAnalysisComponent,
    TagTableComponent,
  ],
})
export class PlannerPopupPoiComponent {
  private readonly service = inject(PlannerPopupService);
  readonly response = this.service.poiResponse;
  protected readonly mainTags = computed(() =>
    InterpretedTags.all(this.response().result.analysis.mainTags)
  );
  protected readonly extraTags = computed(() =>
    InterpretedTags.all(this.response().result.analysis.extraTags)
  );
}
