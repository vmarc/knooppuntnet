import {ChangeDetectionStrategy} from "@angular/core";
import {Component, OnInit} from "@angular/core";
import {PageService} from "../../components/shared/page.service";
import {Store} from "@ngrx/store";
import {selectDemoEnabled} from "../../core/demo/demo.selectors";

@Component({
  selector: "kpn-demo-menu",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <div *ngIf="enabled$ | async; then enabled else disabled"></div>

    <ng-template #enabled>
      <p i18n="demo.select-video">
        Select a video on the left by clicking its play button.
      </p>
    </ng-template>

    <ng-template #disabled>
      <kpn-demo-disabled></kpn-demo-disabled>
    </ng-template>

    <div class="video-icon">
      <mat-icon svgIcon="video"></mat-icon>
    </div>
  `,
  styles: [`
    .video-icon {
      padding-top: 5em;
      display: flex;
      justify-content: center;
    }

    .video-icon mat-icon {
      width: 200px;
      height: 200px;
      color: #f8f8f8;
    }
  `]
})
export class DemoMenuComponent implements OnInit {

  enabled$ = this.store.select(selectDemoEnabled);

  constructor(private store: Store, private pageService: PageService) {
  }

  ngOnInit(): void {
    this.pageService.defaultMenu();
  }

}
