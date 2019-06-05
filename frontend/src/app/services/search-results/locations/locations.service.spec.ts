import { TestBed } from '@angular/core/testing';

import { LocationResultsService } from './location-results.service';

describe('LocationResultsService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: LocationResultsService = TestBed.get(LocationResultsService);
    expect(service).toBeTruthy();
  });
});
