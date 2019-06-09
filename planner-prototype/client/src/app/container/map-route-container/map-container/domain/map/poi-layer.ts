import MVT from "ol/format/MVT";
import VectorTileSource from "ol/source/VectorTile";
import VectorTileLayer from "ol/layer/VectorTile";
import Feature from "ol/Feature";
import {PoiStyle} from "../../../../../model";

const poiStyle = new PoiStyle();
const source = new VectorTileSource({
  url: "/tiles/poi/{z}/{x}/{y}.mvt",
  format: new MVT({
    featureClass: Feature
  })
});

export class PoiLayer {

  createShopLayer(title: string): VectorTileLayer {
    return new VectorTileLayer({
      title: title,
      visible: false,
      source: source,
      zIndex: 25,
      style: (feature) => poiStyle.shopStyleMap.get(feature.values_.layer)
    });
  }

  createCulturalLayer(title: string): VectorTileLayer {
    return new VectorTileLayer({
      title: title,
      visible: false,
      source: source,
      zIndex: 25,
      style: (feature) => poiStyle.culturalStyleMap.get(feature.values_.layer)
    });
  }

  createPublicLayer(title: string): VectorTileLayer {
    return new VectorTileLayer({
      title: title,
      visible: false,
      source: source,
      zIndex: 25,
      style: (feature) => poiStyle.publicStyleMap.get(feature.values_.layer)
    });
  }

  createDrinkAndFoodLayer(title: string): VectorTileLayer {
    return new VectorTileLayer({
      title: title,
      visible: false,
      source: source,
      zIndex: 25,
      style: (feature) => poiStyle.foodAndDrinkStyleMap.get(feature.values_.layer)
    });
  }

  createHotelLayer(title: string): VectorTileLayer {
    return new VectorTileLayer({
      title: title,
      visible: false,
      source: source,
      zIndex: 25,
      style: (feature) => poiStyle.hotelStyleMap.get(feature.values_.layer)
    });
  }

  createSportLayer(title: string): VectorTileLayer {
    return new VectorTileLayer({
      title: title,
      visible: false,
      source: source,
      zIndex: 25,
      style: (feature) => poiStyle.sportStyleMap.get(feature.values_.layer)
    });
  }
}
