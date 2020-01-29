import {PdfStripDocumentModel} from "./pdf-strip-document-model";
import {PdfPlanNode} from "./pdf-plan-node";
import {List} from "immutable";

describe("PdfStripDocumentModel", () => {

  function createModel(nodeCount: number): PdfStripDocumentModel {
    const nodes = new Array<PdfPlanNode>();
    for (let i = 0; i < nodeCount; i++) {
      const nodeName = (i + 1).toString();
      const distance = i === (nodeCount - 1) ? "END" : "10 m";
      const cumulativeDistance = i === 0 ? "START" : `${i * 10} m`;
      nodes.push(new PdfPlanNode(nodeName, distance, cumulativeDistance));
    }
    return new PdfStripDocumentModel(List(nodes));
  }

  it("pageCount", () => {
    expect(createModel(1).pageCount()).toEqual(1);
    expect(createModel(10).pageCount()).toEqual(1);
    expect(createModel(69).pageCount()).toEqual(1);
    expect(createModel(70).pageCount()).toEqual(1);
    expect(createModel(71).pageCount()).toEqual(2);
    expect(createModel(140).pageCount()).toEqual(2);
    expect(createModel(141).pageCount()).toEqual(3);
  });

  it("nodeCountOnPage", () => {
    expect(createModel(1).nodeCountOnPage(0)).toEqual(1);
    expect(createModel(10).nodeCountOnPage(0)).toEqual(10);
    expect(createModel(69).nodeCountOnPage(0)).toEqual(69);
    expect(createModel(70).nodeCountOnPage(0)).toEqual(70);

    expect(createModel(71).nodeCountOnPage(0)).toEqual(70);
    expect(createModel(71).nodeCountOnPage(1)).toEqual(1);

    expect(createModel(72).nodeCountOnPage(0)).toEqual(70);
    expect(createModel(72).nodeCountOnPage(1)).toEqual(2);

    expect(createModel(140).nodeCountOnPage(0)).toEqual(70);
    expect(createModel(140).nodeCountOnPage(1)).toEqual(70);
    expect(createModel(141).nodeCountOnPage(2)).toEqual(1);
  });

  it("columnCountOnPage", () => {
    expect(createModel(1).columnCountOnPage(0)).toEqual(1);
    expect(createModel(14).columnCountOnPage(0)).toEqual(1);
    expect(createModel(15).columnCountOnPage(0)).toEqual(2);
    expect(createModel(28).columnCountOnPage(0)).toEqual(2);
    expect(createModel(29).columnCountOnPage(0)).toEqual(3);

    expect(createModel(69).columnCountOnPage(0)).toEqual(5);
    expect(createModel(70).columnCountOnPage(0)).toEqual(5);
    expect(createModel(71).columnCountOnPage(1)).toEqual(1);
    expect(createModel(72).columnCountOnPage(1)).toEqual(1);

    expect(createModel(141).columnCountOnPage(0)).toEqual(5);
    expect(createModel(141).columnCountOnPage(1)).toEqual(5);
    expect(createModel(141).columnCountOnPage(2)).toEqual(1);
  });

  it("calculateRowCount", () => {

    const model = createModel(80);

    expect(model.calculateRowCount(1, 1, 0)).toEqual(1);

    expect(model.calculateRowCount(14, 1, 0)).toEqual(14);

    expect(model.calculateRowCount(15, 2, 0)).toEqual(14);
    expect(model.calculateRowCount(15, 2, 1)).toEqual(1);
  });

  it("node", () => {
    const model = createModel(80);
    expect(model.node(0, 0, 0).nodeName).toEqual("1");
    expect(model.node(0, 0, 1).nodeName).toEqual("2");
    expect(model.node(0, 1, 0).nodeName).toEqual("15");
    expect(model.node(0, 2, 0).nodeName).toEqual("29");
    expect(model.node(1, 0, 0).nodeName).toEqual("71");
  });

});
