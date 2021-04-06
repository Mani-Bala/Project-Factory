import { TimeoutConstants } from './../constant/enums.enum';
import { isNullOrUndefined } from 'util';
import { Directive, OnInit, ElementRef } from '@angular/core';
import * as _ from 'lodash';
import { StorageService } from './../services/storage/storage.service';
import { AppUtilsService } from '../services/utils/app-utils.service';

@Directive({
  selector: '[appColumnWidth]'
})
export class ColumnWidthDirective implements OnInit {
  constructor(private el: ElementRef, private storage: StorageService, private appUtils: AppUtilsService) {
    this.table = el.nativeElement;
  }
  table: HTMLTableElement;
  tagNamesToExclude: string[] = ['IMG', 'MDB-CHECKBOX'];
  classNamesToExclude = 'badge';
  classNamesToExcludeForLastColumn: string[] = ['action-column-icons', 'header-column'];

  pageX: number;
  currentColumn: HTMLElement;
  nextColumn: HTMLElement;
  currentColumnWidth: number;
  nextColumnWidth: number;
  parentOfCurrentColumn: HTMLElement;
  parentColumnChildren: HTMLCollection;
  requiredColumnsRows: NodeListOf<Element>;
  requiredColumnNumber: number;
  columnToApplyStyle: HTMLElement;
  tableElement: HTMLElement;
  mouseMoveListener = e => this.applyWidth(e, this);
  mouseUpListener = e => this.removeListener(e, this);
  ngOnInit() {
    const self = this;
    const checkExist = setInterval(function() {
      if (self.table) {
        self.resizeTable();
        clearInterval(checkExist);
      }
    }, TimeoutConstants.TIMEOUT_500);
  }

  resizeTable() {
    const self = this;
    const row = self.table.getElementsByTagName('tr')[0];
    const cols = row ? row.children : null;

    if (!cols) {
      // if no columns are available
      return;
    }

    // Getting rows which are not hidden in UI
    const nonHiddenRows = _.filter(cols, function(col) {
      return !col.hasAttribute('hidden');
    });

    let maxColumns = 0;
    // checking if hover action column is there in table
    if (_.intersection(this.classNamesToExcludeForLastColumn, row.children[row.children.length - 1].classList).length > 0) {
      // hover action column is there in table
      maxColumns = nonHiddenRows.length - 2;
    } else {
      // hover action column is not there in table
      maxColumns = nonHiddenRows.length - 1;
    }

    _.forEach(nonHiddenRows, function(el: HTMLElement, i: number) {
      if (maxColumns === 0) {
        if (i === 0) {
          // setting width for 1st column as 100 %
          self.setWidthInGivenPercentageForOneColumn(100, i);
        }
        self.deleteIfAlreadyPresent(el);
        return false;
      }

      if (maxColumns === i) {
        // setting width for last column as 100 % for the table which has 2 columns
        if (maxColumns < 2) {
          self.setWidthInGivenPercentageForOneColumn(100, i);
        }
        // removing the resize for last column
        self.deleteIfAlreadyPresent(el);
        return false;
      }

      self.deleteIfAlreadyPresent(el);
      self.createResizeDiv(el);
    });
  }

  createResizeDiv(el) {
    const div = this.createDiv(this.table.offsetHeight);
    const column = el as HTMLElement;
    column.appendChild(div);
    column.style.position = 'relative';
    this.setListeners(div);
  }

  deleteIfAlreadyPresent(column) {
    const ele = column.querySelector('.column-resize');
    if (!isNullOrUndefined(ele)) {
      ele.remove();
    }
  }

  createDiv(height) {
    const div = document.createElement('div') as HTMLElement;
    div.style.height = height + 'px';
    div.className = 'column-resize';
    return div;
  }

  setListeners(div) {
    const self = this;
    self.setDataAsNull();
    div.addEventListener('mousedown', function(event) {
      self.tableElement = event.target.parentElement.closest('table');
      if (!isNullOrUndefined(self.tableElement)) {
        self.tableElement.style.cursor = 'col-resize';
      }
      self.currentColumn = event.target.parentElement;
      self.nextColumn = self.currentColumn.nextElementSibling as HTMLElement;
      self.pageX = event.pageX;
      self.currentColumnWidth = self.currentColumn.offsetWidth;
      if (!isNullOrUndefined(self.nextColumn)) {
        self.nextColumnWidth = self.nextColumn.offsetWidth;
      }

      // Current Column Element
      self.parentOfCurrentColumn = self.currentColumn.parentElement;
      self.parentColumnChildren = self.parentOfCurrentColumn.children;
      self.requiredColumnNumber = null;

      _.forEach(self.parentColumnChildren, function(col, i) {
        if (self.currentColumn === col) {
          self.requiredColumnNumber = i;
        }
      });

      self.requiredColumnsRows = self.currentColumn.closest('table').querySelectorAll('tbody tr');
      document.addEventListener('mousemove', self.mouseMoveListener);
      document.addEventListener('mouseup', self.mouseUpListener);
      // self.appUtils.getTableData(self.table.getAttribute('id'));
    });
  }

  setDataAsNull() {
    this.pageX = null;
    this.currentColumn = null;
    this.nextColumn = null;
    this.currentColumnWidth = null;
    this.nextColumnWidth = null;
    this.parentOfCurrentColumn = null;
    this.parentColumnChildren = null;
    this.requiredColumnsRows = null;
    this.requiredColumnNumber = null;
    this.columnToApplyStyle = null;
  }

  removeListener(event, self) {
    self.setDataAsNull();
    if (!isNullOrUndefined(self.tableElement)) {
      self.tableElement.style.cursor = '';
    }
    document.removeEventListener('mousemove', self.mouseMoveListener);
    document.removeEventListener('mouseup', self.mouseUpListener);
    this.resizeTable();
  }

  applyWidth(event, self) {
    if (!isNullOrUndefined(self.currentColumn)) {
      const diffX = event.pageX - self.pageX;
      if (!isNullOrUndefined(self.nextColumn)) {
        self.nextColumn.style.width = self.nextColumnWidth - diffX + 'px';
      }
      self.currentColumn.style.width = self.currentColumnWidth + diffX + 'px';
      // Update Header style
      const currentColumnCalWidth = self.currentColumnWidth + diffX + 'px';
      self.currentColumn.style.width = currentColumnCalWidth;
      self.currentColumn.style.minWidth = currentColumnCalWidth;
      self.currentColumn.style.maxWidth = currentColumnCalWidth;

      // Update current column style for ellipsis
      _.forEach(self.requiredColumnsRows, function(col) {
        self.columnToApplyStyle = col.children[self.requiredColumnNumber];
        if (!isNullOrUndefined(self.columnToApplyStyle)) {
          if (
            !_.isEmpty(self.columnToApplyStyle.children) &&
            !isNullOrUndefined(self.columnToApplyStyle.children[0]) &&
            self.columnToApplyStyle.children[0] instanceof HTMLElement &&
            !_.includes(self.tagNamesToExclude, self.columnToApplyStyle.children[0].tagName) &&
            !self.columnToApplyStyle.children[0].className.includes(self.classNamesToExclude)
          ) {
            self.columnToApplyStyle = self.columnToApplyStyle.children[0];
          }
          self.columnToApplyStyle.style.maxWidth = self.currentColumn.style.width;
          self.columnToApplyStyle.style.minWidth = self.currentColumn.style.width;
          self.columnToApplyStyle.style.width = self.currentColumn.style.width;
        }
      });
    }
  }

  resizeForSpecificTable(table) {
    this.table = table;
    this.resizeTable();
  }

  setWidthInGivenPercentageForOneColumn(percentage: number, index: number) {
    const requiredTableHeader: Element = this.table.querySelector('thead tr'); // table header row
    const requiredColumnsRows: NodeListOf<Element> = this.table.querySelectorAll('tbody tr'); // table body rows
    // Header
    const headerToApplyStyle: HTMLElement = requiredTableHeader.children[index] as HTMLElement;
    headerToApplyStyle.style.maxWidth = `${percentage}%`;
    headerToApplyStyle.style.minWidth = `${percentage}%`;
    headerToApplyStyle.style.width = `${percentage}%`;
    const self = this;
    // Individual Columns

    _.forEach(requiredColumnsRows, function(row: HTMLElement) {
      let columnToApplyStyle: HTMLElement = row.children[index] as HTMLElement;
      if (!isNullOrUndefined(columnToApplyStyle) && !columnToApplyStyle.hasAttribute('hidden')) {
        if (
          !_.isEmpty(columnToApplyStyle.children) &&
          !isNullOrUndefined(columnToApplyStyle.children[0]) &&
          columnToApplyStyle.children[0] instanceof HTMLElement &&
          !_.includes(self.tagNamesToExclude, columnToApplyStyle.children[0].tagName) &&
          !columnToApplyStyle.children[0].className.includes(self.classNamesToExclude)
        ) {
          columnToApplyStyle = columnToApplyStyle.children[0] as HTMLElement;
        }
        columnToApplyStyle.style.maxWidth = `${percentage}%`;
        columnToApplyStyle.style.minWidth = `${percentage}%`;
        columnToApplyStyle.style.width = `${percentage}%`;
      }
    });
  }
}
