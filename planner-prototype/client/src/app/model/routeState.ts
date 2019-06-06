import {SelectedRoute} from "./selectedroute";
import Feature from 'ol/Feature';

export class RouteState {

  selectedRoute: SelectedRoute = new SelectedRoute();
  selectedFeatures: Feature[] = [];

  addFeature(feature: Feature) {
    this.selectedRoute.addNode(feature.values_.id);
    this.selectedRoute.addName(feature.values_.name);

    if (this.selectedFeatures.findIndex(f => f.values_.id === feature.values_.id) < 0) {
      this.selectedFeatures.push(feature);
    }
  }

  replaceFeature(draggedNode: Feature, droppedNode: Feature) {
    let indexDropped = this.selectedFeatures.findIndex(f => f.values_.id === droppedNode.values_.id);
    let indexDragged = this.selectedFeatures.findIndex(f => f.values_.id === draggedNode.values_.id);

    this.selectedRoute.replaceNode(draggedNode, droppedNode);

    if (indexDragged > -1 && indexDropped < 0) {
      this.selectedFeatures[indexDragged] = droppedNode;
    } else if (indexDragged > -1 && indexDropped > -1) {
      this.selectedFeatures.splice(indexDragged, 1);
    } else if (indexDragged < 0 && indexDropped > -1) {
      this.selectedFeatures[indexDropped] = draggedNode;
    } else {
      this.addFeature(droppedNode);
    }
  }

  deleteSection(name: string) {
    const nodeIndex = this.selectedRoute.selectedNamesByUser.indexOf(name);
    const featureIndex = this.selectedFeatures.findIndex(x => x.values_.name === name);

    this.selectedFeatures.splice(featureIndex, 1);
    this.selectedRoute.selectedNodesByUser.splice(nodeIndex, 1);
    this.selectedRoute.selectedNamesByUser.splice(nodeIndex, 1);
  }
}
