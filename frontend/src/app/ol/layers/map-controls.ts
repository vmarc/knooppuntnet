import Collection from 'ol/Collection';
import { defaults as defaultControls } from 'ol/control';
import { Attribution } from 'ol/control';
import { ScaleLine } from 'ol/control';
import { FullScreen } from 'ol/control';
import Control from 'ol/control/Control';

export class MapControls {
  static build(): Collection<Control> {
    const fullScreen = new FullScreen();
    const scaleLine = new ScaleLine();
    const attribution = new Attribution({
      collapsible: false,
    });
    return defaultControls({ attribution: false }).extend([fullScreen, scaleLine, attribution]);
  }
}
