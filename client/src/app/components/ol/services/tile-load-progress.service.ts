import {Injectable} from "@angular/core";
import {Layer} from "ol/layer";
import {BehaviorSubject} from "rxjs";

@Injectable()
export class TileLoadProgressService {

  progress: BehaviorSubject<number> = new BehaviorSubject(0);

  private loading = 0;
  private loaded = 0;

  install(bitmapTileLayer: Layer, vectorTileLayer: Layer, poiTileLayer: Layer): void {
    this.installLayer(bitmapTileLayer);
    this.installLayer(vectorTileLayer);
    this.installLayer(poiTileLayer);
  }

  private installLayer(layer: Layer): void {
    layer.getSource().on("tileloadstart", () => {
      this.loading++;
      this.update();
    });
    layer.getSource().on("tileloadend", () => {
      this.loaded++;
      this.update();
    });
    layer.getSource().on("tileloaderror", () => {
      this.loaded++;
      this.update();
    });
  }

  private update() {
    const value = 100 * this.loaded / this.loading;
    this.progress.next(value);
    if (this.loading === this.loaded) {
      this.loading = 0;
      this.loaded = 0;
    }
  }

}
