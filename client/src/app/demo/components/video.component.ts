import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";
import {Input} from "@angular/core";
import {Output} from "@angular/core";
import {EventEmitter} from "@angular/core";
import {ViewChild} from "@angular/core";
import {ElementRef} from "@angular/core";
import {MatSliderChange} from "@angular/material/slider";

@Component({
  selector: "kpn-video",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <kpn-video-cover *ngIf="!canPlayReceived"></kpn-video-cover>

    <video
      #videoPlayer
      width="1280px"
      height="720px"
      controls
      (canplay)="canplayChanged()"
      (timeupdate)="timeupdateChanged()">
      <source [src]="videoSource" type='video/mp4'/>
      Sorry, your browser doesn't support embedded videos.
    </video>

    <div>
      <mat-slider
        class="progress"
        min="0"
        max="100"
        [value]="progress"
        (change)="sliderChanged($event)"
        (input)="inputChanged($event)">
      </mat-slider>
    </div>
    <div>
      <button (click)="playPause()">Play/Pause</button>
    </div>
  `,
  styles: [`
    video {
      border: 2px solid lightgrey;
    }

    .progress {
      width: 1280px;
    }
  `]
})
export class VideoComponent {

  @Input() videoSource: string;
  @Output() canPlay = new EventEmitter<boolean>();
  @Output() currentTime = new EventEmitter<number>();
  @ViewChild("videoPlayer", {static: false}) videoplayer: ElementRef;

  canPlayReceived = false;
  progress = 0;

  canplayChanged(): void {
    this.canPlayReceived = true;
    this.canPlay.emit(true);
    this.play();
  }

  timeupdateChanged(): void {
    if (this.videoplayer.nativeElement.duration > 0) {
      this.progress = 100 * this.videoplayer.nativeElement.currentTime / this.videoplayer.nativeElement.duration;
    }
  }

  playPause(): void {
    if (this.videoplayer.nativeElement.paused) {
      this.play();
    } else {
      this.pause();
    }
  }

  play(): void {
    this.videoplayer.nativeElement.play();
  }

  pause(): void {
    this.videoplayer.nativeElement.pause();
  }

  isPaused(): boolean {
    return this.videoplayer.nativeElement.paused;
  }

  setDurrentTime(value: number): void {
    this.videoplayer.nativeElement.currentTime = value;
  }

  sliderChanged(event: MatSliderChange) {
    console.log("sliderChanged");
  }

  inputChanged(event) {
    console.log("inputChanged");
    this.videoplayer.nativeElement.currentTime = event.value / 100 * this.videoplayer.nativeElement.duration;
  }

}
