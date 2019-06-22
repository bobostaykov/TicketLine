import { TestBed } from '@angular/core/testing';

import { TicketSessionService } from './ticket-session.service';

describe('TicketSessionService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: TicketSessionService = TestBed.get(TicketSessionService);
    expect(service).toBeTruthy();
  });
});
