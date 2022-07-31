import { MapPosition } from './map-position';

describe('MapPosition', () => {
  it('fromQueryParam', () => {
    expect(MapPosition.fromQueryParam(null)).toEqual(null);
    expect(MapPosition.fromQueryParam(undefined)).toEqual(null);
    expect(MapPosition.fromQueryParam('')).toEqual(null);
    expect(MapPosition.fromQueryParam('bla')).toEqual(null);
    expect(MapPosition.fromQueryParam('1,2')).toEqual(null);
    expect(MapPosition.fromQueryParam('bla,2,3')).toEqual(null);
    expect(MapPosition.fromQueryParam('1,bla,3')).toEqual(null);
    expect(MapPosition.fromQueryParam('1,2,bla')).toEqual(null);

    const mapPosition = MapPosition.fromQueryParam('51.4678986,4.4687844,19');
    expect(mapPosition.x).toEqual(497462.80387292453);
    expect(mapPosition.y).toEqual(6704480.657896336);
    expect(mapPosition.zoom).toEqual(19);
    expect(mapPosition.rotation).toEqual(0);
  });
});
