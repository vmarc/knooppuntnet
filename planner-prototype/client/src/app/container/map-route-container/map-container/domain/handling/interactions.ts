import Select from 'ol/interaction/Select';
import Style from 'ol/style/Style';
import {pointerMove} from 'ol/events/condition';


export function createMoveInteraction() {
  return new Select({
    condition: pointerMove,
    multi: false,
    style: new Style()
  });
}


