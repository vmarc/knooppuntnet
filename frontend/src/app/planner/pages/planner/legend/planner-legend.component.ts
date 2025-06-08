import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { LegendItem } from './legend-item';
import { PlannerLegendSectionComponent } from './planner-legend-section.component';
import { State } from '@app/state/state';

@Component({
  selector: 'ui-planner-legend',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (mapMode(); as mapMode) {
      @if (mapMode === 'surface') {
        <ui-planner-legend-section [legendItems]="surfaceLegendItems" />
      }
      @if (mapMode === 'survey') {
        <ui-planner-legend-section [legendItems]="surveyLegendItems" />
      }
      @if (mapMode === 'analysis') {
        <ui-planner-legend-section [legendItems]="analysisLegendItems" />
      }
      <div class="legend-section">
        <ui-planner-legend-section [legendItems]="markerLegendItems" />
      </div>
    }
  `,
  imports: [PlannerLegendSectionComponent],
})
export class PlannerLegendComponent {
  private readonly state = inject(State);
  protected readonly mapMode = this.state.map.mode;

  protected readonly surfaceLegendItems: LegendItem[] = [
    {
      color: 'rgba(0, 96, 255)',
      label: $localize`:@@planner.legend.paved:Paved`,
    },
    {
      color: 'rgb(0, 240, 0)',
      label: $localize`:@@planner.legend.unpaved:Unpaved`,
    },
    {
      color: 'rgb(255, 176, 0)',
      label: $localize`:@@planner.legend.surface-unknown:Surface unknown`,
    },
    {
      color: 'rgb(0, 96, 255)',
      label: $localize`:@@planner.legend.proposed:Proposed`,
      proposed: true,
    },
  ];

  protected readonly surveyLegendItems: LegendItem[] = [
    {
      color: 'rgb(0, 255, 0)',
      label: $localize`:@@planner.legend.survey.last-month:Last month`,
    },
    {
      color: 'rgb(0, 200, 0)',
      label: $localize`:@@planner.legend.survey.last-half-month:Last half year`,
    },
    {
      color: 'rgb(0, 150, 0)',
      label: $localize`:@@planner.legend.survey.last-year:Last year`,
    },
    {
      color: 'rgb(0, 90, 0)',
      label: $localize`:@@planner.legend.survey.last-two-years:Last two years`,
    },
    {
      color: 'rgb(150, 0, 0)',
      label: $localize`:@@planner.legend.survey.more-than-tow-years-ago:More than two years ago`,
    },
    {
      color: 'rgb(255, 255, 0)',
      circleColor: 'rgb(225, 225, 0)',
      label: $localize`:@@planner.legend.survey.unknown:Unknown`,
    },
  ];

  protected readonly analysisLegendItems: LegendItem[] = [
    {
      color: 'rgb(0, 200, 0)',
      label: $localize`:@@planner.legend.analysis.ok:OK`,
    },
    {
      color: 'rgb(0, 150, 0)',
      label: $localize`:@@planner.legend.analysis.ok-orphan:OK Orphan`,
    },
    {
      color: 'rgb(255, 0, 0)',
      label: $localize`:@@planner.legend.analysis.review:Review`,
    },
    {
      color: 'rgb(187, 0, 0)',
      label: $localize`:@@planner.legend.analysis.review-orphan:Review Orphan`,
    },
  ];

  protected readonly markerLegendItems: LegendItem[] = [
    {
      src: '/assets/images/marker-icon-blue.png',
      alt: $localize`:@@planner.legend.marker.icon.start-node:Start node icon`,
      label: $localize`:@@planner.legend.marker.start-node:Start node`,
    },
    {
      src: '/assets/images/marker-icon-green.png',
      alt: $localize`:@@planner.legend.marker.icon.end-node:End node icon`,
      label: $localize`:@@planner.legend.marker.end-node:End node`,
    },
    {
      src: '/assets/images/marker-icon-orange.png',
      alt: $localize`:@@planner.legend.marker.icon.via-node:Via node icon`,
      label: $localize`:@@planner.legend.marker.via-node:Via node`,
    },
  ];
}
