import { TestBed } from '@angular/core/testing';

import { EventResultsService } from './event-results.service';

describe('EventResultsService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: EventResultsService = TestBed.get(EventResultsService);
    expect(service).toBeTruthy();
  });
});
