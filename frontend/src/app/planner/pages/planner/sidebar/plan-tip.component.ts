import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Translations } from '@app/i18n';
import { ZoomLevel } from '@app/ol/domain';
import { MapZoomService } from '@app/ol/services';
import { Plan } from '../../../domain/plan/plan';
import { PlanPhase } from '../../../domain/plan/plan-phase';
import { PlannerService } from '../../../planner.service';

@Component({
  selector: 'kpn-plan-tip',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (planPhase(); as planPhase) {
      <div class="tip">
        @if (planPhaseEnum.zoomInClickStartNode === planPhase) {
          <p i18n="@@planner-tip.zoom-in-click-start-node">
            Zoom in or use magnifying glass to find the startnode of your route.
          </p>
        }
        @if (planPhaseEnum.clickStartNode === planPhase) {
          <p i18n="@@planner-tip.click-start-node">
            Use zoom, pan or use magnifying glass to find and click the startnode of your route.
          </p>
        }
        @if (planPhaseEnum.zoomInClickEndNode === planPhase) {
          <p i18n="@@planner-tip.zoom-in-click-end-node">
            Zoom in and click the endnode of your route.
          </p>
        }
        @if (planPhaseEnum.clickEndNode === planPhase) {
          <p i18n="@@planner-tip.click-end-node">Click the endnode of your route.</p>
        }
        @if (planPhaseEnum.extendRoute === planPhase) {
          <p>
            <ng-container i18n="@@planner-tip.extend-route">
              Extend or adapt your route. Output when satisfied.
            </ng-container>
            <a
              id="read-more"
              [href]="more()"
              i18n="@@planner-tip.read-more"
              target="knooppuntnet-documentation"
              >read more</a
            >
          </p>
        }
      </div>
    }
  `,
  styles: `
    .tip {
      height: 70px;
      font-style: italic;
    }

    #read-more {
      padding-left: 10px;
    }

    #read-more::before {
      content: '(';
      color: black;
    }

    #read-more::after {
      content: ')';
      color: black;
    }
  `,
  standalone: true,
})
export class PlanTipComponent {
  private readonly plannerService = inject(PlannerService);
  private readonly mapZoomService = inject(MapZoomService);

  protected planPhase = computed(() => {
    const zoomLevel = this.mapZoomService.zoomLevel();
    const plan = this.plannerService.context.plan();
    return this.determinePlanPhase(zoomLevel, plan);
  });
  protected planPhaseEnum = PlanPhase;

  more(): string {
    const languageSpecificSubject = Translations.get(`@@wiki.planner.edit`);
    return `https://wiki.openstreetmap.org/wiki/${languageSpecificSubject}`;
  }

  private determinePlanPhase(zoomLevel: number, plan: Plan): PlanPhase {
    let planPhase: PlanPhase;
    if (plan.sourceNode === null) {
      if (zoomLevel < ZoomLevel.vectorTileMinZoom) {
        planPhase = PlanPhase.zoomInClickStartNode;
      } else {
        planPhase = PlanPhase.clickStartNode;
      }
    } else if (plan.legs.isEmpty()) {
      if (zoomLevel < ZoomLevel.vectorTileMinZoom) {
        planPhase = PlanPhase.zoomInClickEndNode;
      } else {
        planPhase = PlanPhase.clickEndNode;
      }
    } else {
      planPhase = PlanPhase.extendRoute;
    }
    return planPhase;
  }
}
