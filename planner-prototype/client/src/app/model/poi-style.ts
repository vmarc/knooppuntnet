import {Icon, Style} from "ol/style";
import {PoiLayers} from "./poi-layers";

const iconURL = "../../assets/images/poi-icons/";
const poiLayers = new PoiLayers();

export class PoiStyle {

  sportStyleMap: Map<string, Style> = new Map<string, Style>();
  publicStyleMap: Map<string, Style> = new Map<string, Style>();
  shopStyleMap: Map<string, Style> = new Map<string, Style>();
  foodAndDrinkStyleMap: Map<string, Style> = new Map<string, Style>();
  culturalStyleMap: Map<string, Style> = new Map<string, Style>();
  hotelStyleMap: Map<string, Style> = new Map<string, Style>();

  constructor() {
    this.initMaps()
  }

  private initMaps() {
    poiLayers.sportPoi.forEach(layer => this.sportStyleMap.set(layer, new Style({
      image: new Icon({
        src: iconURL + layer + ".png"
      })
    })));

    poiLayers.culturalPoi.forEach(layer => this.culturalStyleMap.set(layer, new Style({
      image: new Icon({
        src: iconURL + layer + ".png"
      })
    })));

    poiLayers.publicPoi.forEach(layer => this.publicStyleMap.set(layer, new Style({
      image: new Icon({
        src: iconURL + layer + ".png"
      })
    })));

    poiLayers.foodAndDrinkPoi.forEach(layer => this.foodAndDrinkStyleMap.set(layer, new Style({
      image: new Icon({
        src: iconURL + layer + ".png"
      })
    })));

    poiLayers.shopPoi.forEach(layer => this.shopStyleMap.set(layer, new Style({
      image: new Icon({
        src: iconURL + layer + ".png"
      })
    })));

    poiLayers.hotelPoi.forEach(layer => this.hotelStyleMap.set(layer, new Style({
      image: new Icon({
        src: iconURL + layer + ".png"
      })
    })));
  }
}

