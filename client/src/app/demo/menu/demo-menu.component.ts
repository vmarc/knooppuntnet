import {ChangeDetectionStrategy} from "@angular/core";
import {Component, OnInit} from "@angular/core";
import {PageService} from "../../components/shared/page.service";

@Component({
  selector: "kpn-demo-menu",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p i18n="demo.select-video">
      Select a video on the left by clicking its play button.
    </p>
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

  constructor(private pageService: PageService) {
  }

  ngOnInit(): void {
    this.pageService.defaultMenu();
  }

}
