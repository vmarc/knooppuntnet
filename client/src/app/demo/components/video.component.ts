import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";
import {Input} from "@angular/core";
import {Output} from "@angular/core";
import {EventEmitter} from "@angular/core";
import {ViewChild} from "@angular/core";
import {ElementRef} from "@angular/core";

@Component({
  selector: "kpn-video",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <kpn-video-cover *ngIf="!canPlayReceived"></kpn-video-cover>

    <video
      #videoPlayer
      width="1280px"
      height="720px"
      (canplay)="canplayChanged()"
      (timeupdate)="timeupdateChanged()">
      <source [src]="videoSource" type='video/mp4'/>
      Sorry, your browser doesn't support embedded videos.
    </video>
  `,
  styles: [`
    video {
      border: 2px solid lightgrey;
    }
  `]
})
export class VideoComponent {

  @Input() videoSource: string;
  @Output() canPlay = new EventEmitter<boolean>();
  @Output() currentTime = new EventEmitter<number>();
  @ViewChild("videoPlayer", {static: false}) videoplayer: ElementRef;

  canPlayReceived = false;

  canplayChanged(): void {
    this.canPlayReceived = true;
    this.canPlay.emit(true);
  }

  timeupdateChanged(): void {
    this.currentTime.emit(this.videoplayer.nativeElement.currentTime);
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

}
