import { Injectable } from '@angular/core';
import { Pagination } from '../../models/pagination';

@Injectable({
  providedIn: 'root'
})
export class PaginationUtilsService {
  constructor() {}

  changePage(event: any, pagination: Pagination) {
    if (event.target.text >= 1 && event.target.text <= pagination.numberOfPaginators) {
      pagination.activePage = +event.target.text;
      pagination.firstVisibleIndex = pagination.activePage * pagination.itemsPerPage - pagination.itemsPerPage + 1;
      pagination.lastVisibleIndex = pagination.activePage * pagination.itemsPerPage;
    }
  }

  nextPage(event: any, pagination: Pagination) {
    if (event.last.nativeElement.classList.contains('active')) {
      if (pagination.numberOfPaginators - pagination.numberOfVisiblePaginators >= pagination.lastVisiblePaginator) {
        pagination.firstVisiblePaginator += pagination.numberOfVisiblePaginators;
        pagination.lastVisiblePaginator += pagination.numberOfVisiblePaginators;
      } else {
        pagination.firstVisiblePaginator += pagination.numberOfVisiblePaginators;
        pagination.lastVisiblePaginator = pagination.numberOfPaginators;
      }
    }

    pagination.activePage += 1;
    pagination.firstVisibleIndex = pagination.activePage * pagination.itemsPerPage - pagination.itemsPerPage + 1;
    pagination.lastVisibleIndex = pagination.activePage * pagination.itemsPerPage;
  }

  previousPage(event: any, pagination: Pagination) {
    if (event.first.nativeElement.classList.contains('active')) {
      if (pagination.lastVisiblePaginator - pagination.firstVisiblePaginator === pagination.numberOfVisiblePaginators) {
        pagination.firstVisiblePaginator -= pagination.numberOfVisiblePaginators;
        pagination.lastVisiblePaginator -= pagination.numberOfVisiblePaginators;
      } else {
        pagination.firstVisiblePaginator -= pagination.numberOfVisiblePaginators;
        pagination.lastVisiblePaginator -= pagination.numberOfPaginators % pagination.numberOfVisiblePaginators;
      }
    }

    pagination.activePage -= 1;
    pagination.firstVisibleIndex = pagination.activePage * pagination.itemsPerPage - pagination.itemsPerPage + 1;
    pagination.lastVisibleIndex = pagination.activePage * pagination.itemsPerPage;
  }

  firstPage(pagination: Pagination) {
    pagination.activePage = 1;
    pagination.firstVisibleIndex = pagination.activePage * pagination.itemsPerPage - pagination.itemsPerPage + 1;
    pagination.lastVisibleIndex = pagination.activePage * pagination.itemsPerPage;
    pagination.firstVisiblePaginator = 0;
    pagination.lastVisiblePaginator = pagination.numberOfVisiblePaginators;
  }

  lastPage(pagination: Pagination) {
    pagination.activePage = pagination.numberOfPaginators;
    pagination.firstVisibleIndex = pagination.activePage * pagination.itemsPerPage - pagination.itemsPerPage + 1;
    pagination.lastVisibleIndex = pagination.activePage * pagination.itemsPerPage;

    if (pagination.numberOfPaginators % pagination.numberOfVisiblePaginators === 0) {
      pagination.firstVisiblePaginator = pagination.numberOfPaginators - pagination.numberOfVisiblePaginators;
      pagination.lastVisiblePaginator = pagination.numberOfPaginators;
    } else {
      pagination.lastVisiblePaginator = pagination.numberOfPaginators;
      pagination.firstVisiblePaginator =
        pagination.lastVisiblePaginator - (pagination.numberOfPaginators % pagination.numberOfVisiblePaginators);
    }
  }

  /** Mini Pagination */

  changeMiniPage(event: any, pagination: Pagination) {
    if (event.target.text >= 1 && event.target.text <= pagination.numberOfPaginators) {
      pagination.activePage = +event.target.text;
      pagination.firstVisibleIndex = pagination.activePage * pagination.miniItemsPerPage - pagination.miniItemsPerPage + 1;
      pagination.miniLastVisibleIndex = pagination.activePage * pagination.miniItemsPerPage;
    }
  }

  nextMiniPage(event: any, pagination: Pagination) {
    if (event.last.nativeElement.classList.contains('active')) {
      if (pagination.numberOfPaginators - pagination.numberOfVisiblePaginators >= pagination.lastVisiblePaginator) {
        pagination.firstVisiblePaginator += pagination.numberOfVisiblePaginators;
        pagination.lastVisiblePaginator += pagination.numberOfVisiblePaginators;
      } else {
        pagination.firstVisiblePaginator += pagination.numberOfVisiblePaginators;
        pagination.lastVisiblePaginator = pagination.numberOfPaginators;
      }
    }

    pagination.activePage += 1;
    pagination.firstVisibleIndex = pagination.activePage * pagination.miniItemsPerPage - pagination.miniItemsPerPage + 1;
    pagination.miniLastVisibleIndex = pagination.activePage * pagination.miniItemsPerPage;
  }

  previousMiniPage(event: any, pagination: Pagination) {
    if (event.first.nativeElement.classList.contains('active')) {
      if (pagination.lastVisiblePaginator - pagination.firstVisiblePaginator === pagination.numberOfVisiblePaginators) {
        pagination.firstVisiblePaginator -= pagination.numberOfVisiblePaginators;
        pagination.lastVisiblePaginator -= pagination.numberOfVisiblePaginators;
      } else {
        pagination.firstVisiblePaginator -= pagination.numberOfVisiblePaginators;
        pagination.lastVisiblePaginator -= pagination.numberOfPaginators % pagination.numberOfVisiblePaginators;
      }
    }

    pagination.activePage -= 1;
    pagination.firstVisibleIndex = pagination.activePage * pagination.miniItemsPerPage - pagination.miniItemsPerPage + 1;
    pagination.miniLastVisibleIndex = pagination.activePage * pagination.miniItemsPerPage;
  }

  firstMiniPage(pagination: Pagination) {
    pagination.activePage = 1;
    pagination.firstVisibleIndex = pagination.activePage * pagination.miniItemsPerPage - pagination.miniItemsPerPage + 1;
    pagination.miniLastVisibleIndex = pagination.activePage * pagination.miniItemsPerPage;
    pagination.firstVisiblePaginator = 0;
    pagination.lastVisiblePaginator = pagination.numberOfVisiblePaginators;
  }

  lastMiniPage(pagination: Pagination) {
    pagination.activePage = pagination.numberOfPaginators;
    pagination.firstVisibleIndex = pagination.activePage * pagination.miniItemsPerPage - pagination.miniItemsPerPage + 1;
    pagination.miniLastVisibleIndex = pagination.activePage * pagination.miniItemsPerPage;

    if (pagination.numberOfPaginators % pagination.numberOfVisiblePaginators === 0) {
      pagination.firstVisiblePaginator = pagination.numberOfPaginators - pagination.numberOfVisiblePaginators;
      pagination.lastVisiblePaginator = pagination.numberOfPaginators;
    } else {
      pagination.lastVisiblePaginator = pagination.numberOfPaginators;
      pagination.firstVisiblePaginator =
        pagination.lastVisiblePaginator - (pagination.numberOfPaginators % pagination.numberOfVisiblePaginators);
    }
  }
}
