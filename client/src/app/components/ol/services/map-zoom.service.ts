import {Injectable} from "@angular/core";
import {BehaviorSubject} from "rxjs";
import {ZoomLevel} from "../domain/zoom-level";

@Injectable({
  providedIn: "root"
})
export class MapZoomService {

  private _zoomLevel$ = new BehaviorSubject<number>(ZoomLevel.minZoom);
  zoomLevel$ = this._zoomLevel$.asObservable();

  updateZoomLevel(zoomLevel: number): void {
    this._zoomLevel$.next(zoomLevel);
  }
}
