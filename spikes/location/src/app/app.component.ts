import {Component, OnDestroy, OnInit} from "@angular/core";

@Component({
  selector: "app-root",
  template: `
    <div>
      Location
    </div>

    <div *ngIf="error !== null">
      ERROR: {{error}}
    </div>

    <div *ngFor="let position of positions">
      {{position}}
    </div>
  `
})
export class AppComponent implements OnInit, OnDestroy {

  watchId: number = null;
  error: string = null;
  positions: Array<string> = [];

  ngOnInit(): void {
    this.start();
  }

  ngOnDestroy(): void {
    this.stop();
  }

  private start(): void {
    if (!navigator.geolocation) {
      this.error = "Not supported by browser";
      return;
    }
    if (this.watchId !== null) {
      this.stop();
    }

    console.log("get position");
    navigator.geolocation.getCurrentPosition(
      (position) => {
        console.log(["position", position]);
        this.positions.push(JSON.stringify(position, null, 2));
      }, err => {
        console.log("error", err);
        this.error = err.message;
      },
      {
        enableHighAccuracy: true
      }
    );

    console.log("watch position");
    this.watchId = navigator.geolocation.watchPosition(
      (position) => {
        this.positions.push(JSON.stringify(position, null, 2));
      }, err => {
        console.log("error", err);
        this.error = err.message;
      },
      {
        enableHighAccuracy: true
      }
    );
  }

  private stop() {
    navigator.geolocation.clearWatch(this.watchId);
    this.watchId = null;
  }

}
