import Geocoder from 'ol-geocoder';

export function createNominatimGeocoder() {
  return new Geocoder('nominatim', {
    provider: 'photon',
    targetType: 'text-input',
    lang: 'en',
    placeholder: 'Search for ...',
    limit: 5,
    keepOpen: false
  });
}

