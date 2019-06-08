import {Feature} from "ol";

export class SelectedRoute {

  selectedNodesByUser: number[] = [];
  selectedNamesByUser: string[] = [];

  addNode(nodeId: number) {
    if (this.selectedNodesByUser[this.selectedNodesByUser.length - 1] !== +nodeId) {
      this.selectedNodesByUser.push(+nodeId);
    }
  }

  addName(nodeName: string) {
    if (this.selectedNamesByUser[this.selectedNamesByUser.length - 1] !== nodeName) {
      this.selectedNamesByUser.push(nodeName);
    }
  }

  replaceNode(draggedNode: Feature, droppedNode: Feature) {
    const indexDropped = this.selectedNodesByUser.indexOf(+droppedNode.values_.id);
    const indexDragged = this.selectedNodesByUser.indexOf(+draggedNode.values_.id);

    if (indexDragged > -1 && indexDropped < 0) {
      this.selectedNodesByUser[indexDragged] = +droppedNode.values_.id;
      this.selectedNamesByUser[indexDragged] = droppedNode.values_.name;
    } else if (indexDragged > -1 && indexDropped > -1) {
      this.selectedNodesByUser.splice(indexDragged, 1);
      this.selectedNamesByUser.splice(indexDragged, 1);
    } else if (indexDragged < 0 && indexDropped > -1) {
      this.selectedNodesByUser[indexDropped] = +draggedNode.values_.id;
      this.selectedNamesByUser[indexDropped] = draggedNode.values_.name;
    } else {
      this.addNode(+droppedNode.values_.id);
      this.addName(droppedNode.values_.name);
    }
  }
}
