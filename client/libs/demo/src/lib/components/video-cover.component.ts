import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-video-cover',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="cover">
      <div class="cover-text">
        <p i18n="@@video-cover.text">knooppuntnet demo video</p>
        <mat-icon svgIcon="video" class="video-icon" />
      </div>
    </div>
  `,
  styles: [
    `
      .cover {
        background-color: white;
        width: 1280px;
        height: 720px;
        border: 2px solid lightgray;
      }

      .cover-text {
        padding-top: 200px;
        text-align: center;
        font-size: 20px;
        color: grey;
      }

      .video-icon {
        display: inline-block;
        width: 100px;
        height: 100px;
        color: lightgrey;
      }
    `,
  ],
})
export class VideoCoverComponent {}
