import { Tag } from '@api/custom/tag';
import { Tags } from '@api/custom/tags';
import { InterpretedTags } from './interpreted-tags';

describe('TagFilter', () => {
  it('empty', () => {
    const filter = InterpretedTags.networkTags(new Tags([]));
    expect(filter.isEmpty()).toEqual(true);
    expect(filter.hasStandardTags()).toEqual(false);
    expect(filter.hasExtraTags()).toEqual(false);
    expect(filter.standardTags()).toEqual([]);
    expect(filter.extraTags()).toEqual([]);
  });

  it('tags are filtered out and sorted', () => {
    const unfiltered = new Tags([
      new Tag('c', '6'),
      new Tag('name', '3'),
      new Tag('b', '5'),
      new Tag('type', '2'),
      new Tag('a', '4'),
      new Tag('network', '1'),
    ]);

    const expectedStandardTags = [
      new Tag('network', '1'),
      new Tag('type', '2'),
      new Tag('name', '3'),
    ];

    const expectedExtraTags = [
      new Tag('a', '4'),
      new Tag('b', '5'),
      new Tag('c', '6'),
    ];

    const filter = InterpretedTags.networkTags(unfiltered);

    expect(filter.isEmpty()).toEqual(false);
    expect(filter.hasStandardTags()).toEqual(true);
    expect(filter.hasExtraTags()).toEqual(true);
    expect(filter.standardTags()).toEqual(expectedStandardTags);
    expect(filter.extraTags()).toEqual(expectedExtraTags);
  });
});
