import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PoiAnalysisComponent } from '@app/components/poi';
import { InterpretedTags } from '@app/components/shared/tags';
import { TagsTableComponent } from '@app/components/shared/tags';
import { ActionButtonNodeComponent } from '../../../../analysis/components/action/action-button-node.component';
import { ActionButtonRelationComponent } from '../../../../analysis/components/action/action-button-relation.component';
import { ActionButtonWayComponent } from '../../../../analysis/components/action/action-button-way.component';
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
  protected readonly response = this.service.poiResponse;
  protected readonly mainTags = computed(() =>
    InterpretedTags.all(this.response().result.analysis.mainTags)
  );
  protected readonly extraTags = computed(() =>
    InterpretedTags.all(this.response().result.analysis.extraTags)
  );
}
