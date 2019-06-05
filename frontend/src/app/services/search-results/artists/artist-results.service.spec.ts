import { TestBed } from '@angular/core/testing';

import { ArtistResultsService } from './artist-results.service';

describe('ArtistResultsService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ArtistResultsService = TestBed.get(ArtistResultsService);
    expect(service).toBeTruthy();
  });
});
