import { TestBed, inject } from '@angular/core/testing';

import { PaginationUtilsService } from './pagination-utils.service';

describe('PaginationUtilsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [PaginationUtilsService]
    });
  });

  it('should be created', inject([PaginationUtilsService], (service: PaginationUtilsService) => {
    expect(service).toBeTruthy();
  }));
});
