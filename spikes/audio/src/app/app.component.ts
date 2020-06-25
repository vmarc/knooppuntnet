import {Component, OnInit} from "@angular/core";
import {Range} from "immutable";
import {MatRadioChange} from "@angular/material/radio";

@Component({
  selector: "app-root",
  template: `
    <div class="actions">
      <button mat-stroked-button (click)="one()">One</button>
      <button mat-stroked-button (click)="two()">Two</button>
      <button mat-stroked-button (click)="ten()">Ten</button>
      <button mat-stroked-button (click)="startCounting()">Start counting</button>
      <button mat-stroked-button (click)="stopCounting()">Stop counting</button>
      <mat-radio-group value="1000" (change)="delayChanged($event)" class="delay">
        <mat-radio-button value="1000">1 second</mat-radio-button>
        <mat-radio-button value="10000">10 seconds</mat-radio-button>
        <mat-radio-button value="600000">1 minute</mat-radio-button>
      </mat-radio-group>
    </div>
  `, styles: [`

    .actions {
      margin: 1em;
      display: flex;
      flex-direction: column;
      align-items: center;
    }

    .actions button {
      width: 15em;
      margin: 0.5em;
    }

    .delay {
      margin: 1em;
      display: flex;
      flex-direction: column;
    }

    .delay mat-radio-button {
      margin: 0.3em;
    }
  `]
})
export class AppComponent implements OnInit {

  countingDelay = 1000;
  private numberClips: HTMLAudioElement[];
  private currentClipIndex = 0;
  private countingInterval: number = null;

  ngOnInit(): void {
    this.numberClips = Range(1, 10 + 1).map(name => {
      let audio = new Audio();
      audio.src = `/assets/${name}.mp3`;
      audio.load();
      return audio;
    }).toArray();
  }

  one(): void {
    this.play(0);
  }

  two(): void {
    this.play(1);
  }

  ten(): void {
    this.play(9);
  }

  startCounting(): void {
    this.stopCounting();
    this.currentClipIndex = 0;
    this.play(this.currentClipIndex);
    this.countingInterval = setInterval(
      () => {
        this.currentClipIndex++;
        if (this.currentClipIndex > this.numberClips.length) {
          this.currentClipIndex = 0;
        }
        this.play(this.currentClipIndex);
      },
      this.countingDelay
    );
  }

  stopCounting(): void {
    if (this.countingInterval != null) {
      clearInterval(this.countingInterval);
      this.countingInterval = null;
    }
  }

  delayChanged(event: MatRadioChange): void {
    this.countingDelay = +event.value;
    this.startCounting();
  }

  private play(clipNumber: number): void {
    this.numberClips[clipNumber].play();
  }

}
