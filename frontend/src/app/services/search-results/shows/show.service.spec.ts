import { TestBed } from '@angular/core/testing';

import { ShowResultsService } from './show-results.service';

describe('ShowResultsService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ShowResultsService = TestBed.get(ShowResultsService);
    expect(service).toBeTruthy();
  });
});
