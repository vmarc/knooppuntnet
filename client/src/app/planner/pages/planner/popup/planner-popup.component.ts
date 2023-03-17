import { AfterViewInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PlannerService } from '../../../services/planner.service';

@Component({
  selector: 'kpn-planner-popup',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div id="popup" class="ol-popup" [class.ol-popup-offset]="offset">
      <a
        href="#"
        (click)="closePopup()"
        id="popup-closer"
        class="ol-popup-closer"
      ></a>
      <kpn-planner-popup-contents/>
    </div>
  `,
  styles: [
    `
      .ol-popup {
        position: absolute;
        background-color: white;
        -webkit-filter: drop-shadow(0 1px 4px rgba(0, 0, 0, 0.2));
        filter: drop-shadow(0 1px 4px rgba(0, 0, 0, 0.2));
        padding: 15px;
        border-radius: 10px;
        border: 1px solid #cccccc;
        min-width: 280px;
      }

      .ol-popup-offset {
        bottom: 0;
        left: -50px;
      }

      .ol-popup:after,
      .ol-popup:before {
        top: 100%;
        border: solid transparent;
        content: ' ';
        height: 0;
        width: 0;
        position: absolute;
        pointer-events: none;
      }

      .ol-popup:after {
        border-top-color: white;
        border-width: 10px;
        left: 48px;
        margin-left: -10px;
      }

      .ol-popup:before {
        border-top-color: #cccccc;
        border-width: 11px;
        left: 48px;
        margin-left: -11px;
      }

      .ol-popup-closer {
        text-decoration: none;
        position: absolute;
        top: 2px;
        right: 8px;
      }

      .ol-popup-closer:after {
        content: '✖';
      }
    `,
  ],
})
export class PlannerPopupComponent implements AfterViewInit {
  offset = false;

  constructor(private plannerService: PlannerService) {}

  closePopup() {
    this.plannerService.context.overlay.setPosition(undefined, 0);
    return false;
  }

  ngAfterViewInit(): void {
    setTimeout(() => (this.offset = true), 500);
  }
}
