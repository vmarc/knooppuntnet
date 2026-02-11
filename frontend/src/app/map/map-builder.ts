import { FullscreenControl } from 'maplibre-gl';
import { GeolocateControl } from 'maplibre-gl';
import { NavigationControl } from 'maplibre-gl';
import { Map as MaplibreMap } from 'maplibre-gl';

export class MapBuilder {
  build(): MaplibreMap {
    const map = new MaplibreMap({
      container: 'map',
      style: '/assets/liberty.json',
      // center: [4.46839, 51.46774], // essen
      // zoom: 13,
    });
    map.showTileBoundaries = true;
    map.dragRotate.disable();
    map.touchZoomRotate.disableRotation();
    map.keyboard.disableRotation();
    // map.showCollisionBoxes = true;
    this.preventImageMissingWarning(map);

    map.addControl(new FullscreenControl({}));

    map.addControl(
      new NavigationControl({
        showZoom: true,
        showCompass: false,
        visualizePitch: false,
        visualizeRoll: false,
      })
    );

    map.addControl(new GeolocateControl({}));

    // mapLibreMap.on('zoom', () => {
    //   console.log('zoom changed ' + mapLibreMap.getZoom());
    // });

    return map;
  }

  private preventImageMissingWarning(map: MaplibreMap): void {
    map.on('styleimagemissing', (e) => {
      // Add a transparent image to prevent the warning
      map.addImage(e.id, {
        width: 1,
        height: 1,
        data: new Uint8Array([0, 0, 0, 0]),
      });
    });
  }
}
