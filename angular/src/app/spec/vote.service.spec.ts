import { TestBed } from '@angular/core/testing';

import { VoteService } from '../service/vote.service';

describe('VoteService', () => {
  let service: VoteService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VoteService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
