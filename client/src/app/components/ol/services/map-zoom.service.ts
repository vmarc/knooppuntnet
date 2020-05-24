import {Injectable} from "@angular/core";
import {View} from "ol";
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: "root"
})
export class MapZoomService {

  private _zoomLevel = 0;
  private _zoomLevel$ = new BehaviorSubject<number>(this._zoomLevel);
  zoomLevel$ = this._zoomLevel$.asObservable();

  install(view: View): void {
    this.updateZoomLevel(view.getZoom());
    view.on("change:resolution", () => this.updateZoomLevel(view.getZoom()));
  }

  private updateZoomLevel(zoomLevel: number): void {
    const roundedZoomLevel = Math.round(zoomLevel);
    if (roundedZoomLevel !== this._zoomLevel) {
      this._zoomLevel = roundedZoomLevel;
      this._zoomLevel$.next(roundedZoomLevel);
    }
  }
}
