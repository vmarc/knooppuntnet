import { List } from 'immutable';
import { PdfDocumentModel } from './pdf-document-model';
import { PdfPlanNode } from './pdf-plan-node';

describe('PdfDocumentModel', () => {
  const createModel = (nodeCount: number): PdfDocumentModel => {
    const nodes = new Array<PdfPlanNode>();
    for (let i = 0; i < nodeCount; i++) {
      const nodeName = (i + 1).toString();
      const distance = i === nodeCount - 1 ? 'END' : '10 m';
      const cumulativeDistance = i === 0 ? 'START' : `${i * 10} m`;
      nodes.push(new PdfPlanNode(nodeName, distance, cumulativeDistance, '', '', false));
    }
    return new PdfDocumentModel(List(nodes));
  };

  it('pageCount', () => {
    expect(createModel(1).pageCount()).toEqual(1);
    expect(createModel(10).pageCount()).toEqual(1);
    expect(createModel(97).pageCount()).toEqual(1);
    expect(createModel(98).pageCount()).toEqual(1);
    expect(createModel(99).pageCount()).toEqual(2);
    expect(createModel(196).pageCount()).toEqual(2);
    expect(createModel(197).pageCount()).toEqual(3);
  });

  it('nodeCountOnPage', () => {
    expect(createModel(1).pageNodeCount(0)).toEqual(1);
    expect(createModel(10).pageNodeCount(0)).toEqual(10);
    expect(createModel(97).pageNodeCount(0)).toEqual(97);
    expect(createModel(98).pageNodeCount(0)).toEqual(98);

    expect(createModel(99).pageNodeCount(0)).toEqual(98);
    expect(createModel(99).pageNodeCount(1)).toEqual(1);

    expect(createModel(196).pageNodeCount(0)).toEqual(98);
    expect(createModel(196).pageNodeCount(1)).toEqual(98);

    expect(createModel(197).pageNodeCount(0)).toEqual(98);
    expect(createModel(197).pageNodeCount(1)).toEqual(98);
    expect(createModel(197).pageNodeCount(2)).toEqual(1);
  });

  it('pageRowCount', () => {
    expect(createModel(1).pageRowCount(0)).toEqual(1);
    expect(createModel(7).pageRowCount(0)).toEqual(1);
    expect(createModel(8).pageRowCount(0)).toEqual(2);
    expect(createModel(14).pageRowCount(0)).toEqual(2);
    expect(createModel(15).pageRowCount(0)).toEqual(3);

    expect(createModel(98).pageRowCount(0)).toEqual(14);
    expect(createModel(99).pageRowCount(0)).toEqual(14);
    expect(createModel(99).pageRowCount(1)).toEqual(1);
  });

  it('columnCount', () => {
    const model = createModel(100);

    expect(model.columnCount(0, 1, 0)).toEqual(7);
    expect(model.columnCount(0, 2, 0)).toEqual(7);
    expect(model.columnCount(0, 2, 1)).toEqual(7);

    expect(model.columnCount(1, 1, 0)).toEqual(2);
  });

  it('node', () => {
    const model = createModel(100);
    expect(model.node(0, 0, 0).nodeName).toEqual('1');
    expect(model.node(0, 0, 1).nodeName).toEqual('2');
    expect(model.node(0, 1, 0).nodeName).toEqual('8');
    expect(model.node(0, 2, 0).nodeName).toEqual('15');
    expect(model.node(1, 0, 0).nodeName).toEqual('99');
  });
});
