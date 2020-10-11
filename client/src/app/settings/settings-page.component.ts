import {ChangeDetectionStrategy, Component} from "@angular/core";
import {SettingsService} from "../services/settings.service";
import {MatSlideToggleChange} from "@angular/material/slide-toggle";

@Component({
  selector: "kpn-settings-page",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li i18n="@@breadcrumb.settings">Settings</li>
    </ul>

    <kpn-page-header i18n="@@settings-page.title">Settings</kpn-page-header>

    <mat-slide-toggle [checked]="directions()" (change)="directionsChanged($event)" i18n="@@settings.directions">
      Navigation instructions
    </mat-slide-toggle>

    <p class="comment" i18n="@@settings.directions.comment.1">
      You can enable this extra functionality in the planner to generate
      a list with navigation instructions for the route you have planned.
    </p>
    <p class="comment" i18n="@@settings.directions.comment.2">
      This functionality is still experimental and under development at
      this moment and may not work completely ok yet. By default, this
      functionality is not enabled.
    </p>
  `,
  styles: [`
    .comment {
      margin-left: 1em;
      max-width: 40em;
      font-style: italic;
    }
    .spacer {
      margin-top: 50px;
    }
  `]
})
export class SettingsPageComponent {

  constructor(private service: SettingsService) {
  }

  directions(): boolean {
    return this.service.instructions;
  }

  directionsChanged(event: MatSlideToggleChange): void {
    this.service.instructions = event.checked;
  }
}
