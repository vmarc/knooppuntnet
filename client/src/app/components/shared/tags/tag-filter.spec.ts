import { Tag } from '@api/custom/tag';
import { Tags } from '@api/custom/tags';
import { InterpretedTags } from './interpreted-tags';

describe('TagFilter', () => {
  it('empty', () => {
    const filter = InterpretedTags.networkTags({ tags: [] });
    expect(filter.isEmpty()).toEqual(true);
    expect(filter.hasStandardTags()).toEqual(false);
    expect(filter.hasExtraTags()).toEqual(false);
    expect(filter.standardTags()).toEqual([]);
    expect(filter.extraTags()).toEqual([]);
  });

  it('tags are filtered out and sorted', () => {
    const unfiltered: Tags = {
      tags: [
        { key: 'c', value: '6' },
        { key: 'name', value: '3' },
        { key: 'b', value: '5' },
        { key: 'type', value: '2' },
        { key: 'a', value: '4' },
        { key: 'network', value: '1' },
      ],
    };

    const expectedStandardTags: Tag[] = [
      { key: 'network', value: '1' },
      { key: 'type', value: '2' },
      { key: 'name', value: '3' },
    ];

    const expectedExtraTags: Tag[] = [
      { key: 'a', value: '4' },
      { key: 'b', value: '5' },
      { key: 'c', value: '6' },
    ];

    const filter = InterpretedTags.networkTags(unfiltered);

    expect(filter.isEmpty()).toEqual(false);
    expect(filter.hasStandardTags()).toEqual(true);
    expect(filter.hasExtraTags()).toEqual(true);
    expect(filter.standardTags()).toEqual(expectedStandardTags);
    expect(filter.extraTags()).toEqual(expectedExtraTags);
  });
});
