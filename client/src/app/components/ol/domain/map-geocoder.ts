import Geocoder from 'ol-geocoder';
import Map from 'ol/Map';

export class MapGeocoder {
  static install(map: Map): void {
    const geocoder = new Geocoder('nominatim', {
      provider: 'osm',
      targetType: 'glass-button',
      lang: 'en', // Photon does not support "nl" (only: "de", "it", "fr" and "en")
      placeholder: 'Search for...',
      limit: 5,
      keepOpen: false,
      debug: true,
    });

    map.addControl(geocoder);
    geocoder.on('addresschosen', (evt) =>
      map.getView().animate({ center: evt.coordinate })
    );
  }
}
