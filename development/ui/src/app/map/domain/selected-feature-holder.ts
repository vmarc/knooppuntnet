import {SelectedFeature} from "./selected-feature";

export class SelectedFeatureHolder {

  constructor(public listener: (SelectedFeature) => void) {
  }

  select(selection: SelectedFeature) {
    if (this.listener) {
      this.listener(selection);
    }
  }
}
