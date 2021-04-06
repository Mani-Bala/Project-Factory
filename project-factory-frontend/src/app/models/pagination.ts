import { QueryList } from '@angular/core';

export class Pagination {
  pages: QueryList<any>;
  numberOfVisiblePaginators = 10;
  numberOfPaginators: number;
  firstVisiblePaginator = 0;
  lastVisiblePaginator = this.numberOfVisiblePaginators;
  activePage = 1;
  itemsPerPage = 25;
  miniItemsPerPage = 10;
  firstVisibleIndex = 1;
  lastVisibleIndex = this.itemsPerPage;
  miniLastVisibleIndex = this.miniItemsPerPage;
  pageNumItemOptions = [
    { value: '25', label: '25' },
    { value: '50', label: '50' },
    { value: '100', label: '100' },
    { value: '200', label: '200' }
  ];
  miniPageNumItemOptions = [
    { value: '5', label: '5' },
    { value: '10', label: '10' },
    { value: '20', label: '20' },
    { value: '40', label: '40' }
  ];
}
