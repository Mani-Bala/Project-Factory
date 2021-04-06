// import { TableProperty } from '../../models/table-property';
// import { MDBDatePickerComponent } from 'ng-uikit-pro-standard';
import {ElementRef, Injectable} from '@angular/core';
import * as _ from 'lodash';
// import * as moment from 'moment-timezone';
import {isNullOrUndefined} from 'util';
// import { CurriculumActivity, SystemRole } from '../../constant/enums.enum';
import {StorageService} from '../storage/storage.service';
// import { Batch } from '../../models/batch';
// import { Employee } from '../../models/employee';
// import { FilterOptions } from '../../models/filter-options';
// import { ColumnFilter } from '../../models/column-filter';
@Injectable({
  providedIn: 'root'
})
export class AppUtilsService {
  constructor(private storage: StorageService) {}

  makeDeepCopy(object: any) {
    if (!object) {
      return null;
    }
    return JSON.parse(JSON.stringify(object));
  }

  openExternalURL(url: string, type = '_self') {
    window.open(url, type);
  }

  // getUserTimeZone() {
  //   return this.storage.get(Constants.TIME_ZONE_KEY);
  // }

  // public getLocaleBasedDateFormat() {
  //   const timeZone = this.getUserTimeZone();
  //   if (timeZone === 'Asia/Kolkata' || timeZone === 'Asia/Calcutta') {
  //     return Constants.START_WITH_DATE;
  //   } else if (timeZone === 'America/New_York') {
  //     return Constants.START_WITH_MONTH;
  //   } else {
  //     return Constants.START_WITH_MONTH;
  //   }
  // }

  // convertToUTC(date: any) {
  //   const timeZone = this.storage.get(Constants.TIME_ZONE_KEY);
  //   moment(date).valueOf();
  //   moment(date, timeZone)
  //     .utc()
  //     .format(Constants.START_WITH_MONTH_AND_TIME);
  // }

  // convertToUserTimezone(date: any, timeZone?: string) {
  //   if (isNullOrUndefined(timeZone)) {
  //     timeZone = this.storage.get(Constants.TIME_ZONE_KEY);
  //   }
  //   if (moment(date).isValid()) {
  //     const utcInput = moment.utc(date);
  //     return moment(utcInput).tz(timeZone);
  //   }
  // }

  // convertToTimezone(date: any, timeZone: string) {
  //   if (moment(date).isValid()) {
  //     const utcInput = moment.utc(date);
  //     return moment(utcInput).tz(timeZone);
  //   }
  // }
  //
  // makeClockPickerObject(date: string) {
  //   date = !isNullOrUndefined(date) ? date : moment().format(Constants.TIME_IN_HOUR_MINUTES);
  //   return !isNullOrUndefined(date)
  //     ? {
  //         ampm: date.includes('AM') ? 'AM' : 'PM',
  //         h: date.substring(0, 2),
  //         m: date.substring(3, 5)
  //       }
  //     : {
  //         ampm: 'AM',
  //         h: '12',
  //         m: '00'
  //       };
  // }
  //
  // getCurrentTimeInUserTimezone() {
  //   return moment(new Date())
  //     .tz(this.getUserTimeZone())
  //     .format(Constants.START_WITH_YEAR_AND_TIME_SECONDS);
  // }
  //
  // getCurrentTimeInTimezone(timezone: string) {
  //   return moment(new Date())
  //     .tz(timezone)
  //     .format(Constants.START_WITH_YEAR_AND_TIME_SECONDS);
  // }

  generateUUID() {
    function s4() {
      return Math.floor((1 + Math.random()) * 0x10000)
        .toString(16)
        .substring(1);
    }
    return s4() + s4() + '-' + s4() + '-' + s4() + '-' + s4() + '-' + s4() + s4() + s4();
  }

  findMissingElements(arr) {
    // Make sure the numbers are in order
    arr = arr.slice(0).sort(function(a, b) {
      return a - b;
    });
    let next = 1; // The next number in the sequence
    const missing = [];
    for (let i = 0; i < arr.length; i++) {
      // While the expected element is less than
      // the current element
      while (next < arr[i]) {
        // Add it to the missing list and
        // increment to the next expected number
        missing.push(next);
        next++;
      }
      next++;
    }
    return missing;
  }

  mergeWithoutDuplicates(i, x) {
    const h = {};
    const n = [];
    for (let a = 2; a--; i = x) {
      i.map(function(b) {
        h[b] = h[b] || n.push(b);
      });
    }
    return n;
  }

  reAdjustFooter(footerEl: ElementRef, view: ElementRef) {
    this.onElementHeightChange(document.body, function() {
      footerEl.nativeElement.style.position = 'relative';
      footerEl.nativeElement.style.bottom = 0;
      const mainBodyHeight = view.nativeElement.offsetHeight;
      const windowHeight = window.innerHeight;
      if (mainBodyHeight < windowHeight - footerEl.nativeElement.offsetHeight) {
        footerEl.nativeElement.style.position = 'absolute';
      } else {
        footerEl.nativeElement.style.position = 'relative';
      }
    });
  }

  onElementHeightChange(elm, callback) {
    let lastHeight = elm.clientHeight,
      newHeight;
    const footer = setInterval(function() {
      newHeight = elm.clientHeight;
      if (lastHeight !== newHeight) {
        callback();
      }
      lastHeight = newHeight;

      if (document.readyState === 'complete') {
        callback();
        clearingFooterInterval();
      }
    }, 0);

    function clearingFooterInterval() {
      const footerClearingInterval = setInterval(function() {
        clearInterval(footer);
        clearInterval(footerClearingInterval);
      }, 5000);
    }
  }

  adjustLogoPosition(headerElm: ElementRef, img: ElementRef) {
    let height = headerElm.nativeElement.offsetHeight / 2 - img.nativeElement.offsetHeight;
    if (height < 0) {
      height = 0;
    }
    img.nativeElement.style.marginTop = height + 'px';
  }

  getCurrentYear() {
    const date = new Date();
    return date.getFullYear();
  }

  // checkAccess(roleCode: string) {
  //   if (this.storage.get(Constants.ROLE_CODE_KEY) === roleCode) {
  //     return false;
  //   } else {
  //     return true;
  //   }
  // }

  // public buildDatePickerObjectWithPastDates() {
  //   return {
  //     dayLabels: Constants.DATE_PICKER_DAY_LABELS,
  //     dayLabelsFull: Constants.DATE_PICKER_FULL_DAY_LABELS,
  //     monthLabels: Constants.DATE_PICKER_MONTH_LABELS,
  //     monthLabelsFull: Constants.DATE_PICKER_FULL_MONTH_LABELS,
  //     todayBtnTxt: 'Today',
  //     clearBtnTxt: 'Clear',
  //     closeBtnTxt: 'Close',
  //     dateFormat: this.getLocaleBasedDateFormat().toLowerCase(),
  //     firstDayOfWeek: 'mo',
  //     minYear: new Date().getFullYear() - 2,
  //     maxYear: new Date().getFullYear() + 2,
  //     showTodayBtn: true,
  //     showClearDateBtn: true,
  //     markCurrentDay: true,
  //     markWeekends: undefined,
  //     disableHeaderButtons: false,
  //     showWeekNumbers: false,
  //     height: '100px',
  //     width: '50%',
  //     selectionTxtFontSize: '15px'
  //   };
  // }
  //
  // public buildDatePickerObjectWithDisablePastDates() {
  //   return {
  //     dayLabels: Constants.DATE_PICKER_DAY_LABELS,
  //     dayLabelsFull: Constants.DATE_PICKER_FULL_DAY_LABELS,
  //     monthLabels: Constants.DATE_PICKER_MONTH_LABELS,
  //     monthLabelsFull: Constants.DATE_PICKER_FULL_MONTH_LABELS,
  //     todayBtnTxt: 'Today',
  //     clearBtnTxt: 'Clear',
  //     closeBtnTxt: 'Close',
  //     dateFormat: this.getLocaleBasedDateFormat().toLowerCase(),
  //     firstDayOfWeek: 'mo',
  //     minYear: new Date().getFullYear() - 2,
  //     maxYear: new Date().getFullYear() + 2,
  //     disableUntil: { year: new Date().getFullYear(), month: new Date().getMonth() + 1, day: new Date().getDate() - 1 },
  //     showTodayBtn: true,
  //     showClearDateBtn: true,
  //     markCurrentDay: true,
  //     markWeekends: undefined,
  //     disableHeaderButtons: false,
  //     showWeekNumbers: false,
  //     height: '100px',
  //     width: '50%',
  //     selectionTxtFontSize: '15px'
  //   };
  // }

  isElementInViewport(el) {
    if (!isNullOrUndefined(el)) {
      const rect = el.getBoundingClientRect();
      return (
        rect.top >= 0 &&
        rect.left >= 0 &&
        rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) &&
        rect.right <= (window.innerWidth || document.documentElement.clientWidth)
      );
    } else {
      return 0;
    }
  }

  getAbsoluteDomainUrl(): string {
    if (window && 'location' in window && 'protocol' in window.location && 'host' in window.location) {
      return window.location.protocol + '//' + window.location.host;
    }
    return null;
  }
  // public getDateTime(date: string, time: string) {
  //   return (
  //     moment(date, this.getLocaleBasedDateFormat()).format(Constants.START_WITH_YEAR) +
  //     'T' +
  //     moment(time, Constants.TIME_IN_HOUR_MINUTES).format(Constants.TIME_IN_HOUR_MINUTES_SECONDS)
  //   );
  // }

  removeNilValuesFromObject(data) {
    return _.omitBy(data, _.isNil);
  }

  // public getNextLeastUsedColorByOrder(projectActivities, newActivities) {
  //   const colors = [
  //     { color: '#F26A25', colorOrder: 1 },
  //     {
  //       color: '#347AB8',
  //       colorOrder: 2
  //     },
  //     { color: '#60B75E', colorOrder: 3 },
  //     {
  //       color: '#D9544F',
  //       colorOrder: 4
  //     },
  //     { color: '#946BDE', colorOrder: 5 }
  //   ];
  //   let projects = _.filter(projectActivities, { actCode: CurriculumActivity.PROJECT });
  //   const newlyAddedProjects = _.filter(newActivities, { actCode: CurriculumActivity.PROJECT });
  //   if (!isNullOrUndefined(newlyAddedProjects)) {
  //     projects = projects.concat(newlyAddedProjects);
  //   }
  //   const groupedColors = _.map(_.groupBy(projects, 'colorCode'), function(k, n) {
  //     return { color: n, prjColorCount: k.length };
  //   });
  //   const actualColorCounts = _.map(colors, function(o) {
  //     return {
  //       color: o.color,
  //       prjColorCount: _.find(groupedColors, ['color', o.color]) ? _.find(groupedColors, ['color', o.color]).prjColorCount : 0,
  //       colorOrder: o.colorOrder
  //     };
  //   });
  //   const countOrders = _.orderBy(actualColorCounts, ['prjColorCount', 'colorOrder'], ['asc', 'asc']);
  //   return countOrders[0].color;
  // }

  timeCkeditorContent(data) {
    return data
      .replace(/&nbsp;/gm, '')
      .replace(/>\s+</g, '><')
      .replace(/<[\S]+><\/[\S]+>/gm, '');
  }

  // getTableData(tableId) {
  //   const classNamesToExcludeForLastColumn: string[] = ['action-column-icons', 'header-column'];
  //   const table = document.getElementById(tableId);
  //   let onlyOneColumnAvailable = false;
  //   if (!isNullOrUndefined(table)) {
  //     const row = table.getElementsByTagName('tr')[0];
  //     const data = [];
  //
  //     const nonHiddenRows = _.filter(row.children, function(col) {
  //       return !col.hasAttribute('hidden');
  //     });
  //     let nonHiddenRowsCount = nonHiddenRows.length;
  //
  //     if (_.intersection(classNamesToExcludeForLastColumn, row.children[row.children.length - 1].classList).length > 0) {
  //       nonHiddenRowsCount = nonHiddenRowsCount - 1;
  //       if (nonHiddenRowsCount === 1) {
  //         onlyOneColumnAvailable = true;
  //       }
  //     }
  //
  //     _.forEach(row.children, function(record: HTMLElement, index: number) {
  //       if (index !== row.children.length - 1) {
  //         if (record.style.width === '' || Number(record.style.width.replace('px', '')) === record.offsetWidth) {
  //           data.push(record.offsetWidth);
  //         } else if (record.style.width !== '' && record.offsetWidth !== Number(record.style.width.replace('px', ''))) {
  //           const getPaddingLeft = window.getComputedStyle(record, null).getPropertyValue('padding-left');
  //           const paddingLeft = isNullOrUndefined(getPaddingLeft) ? 0 : Number(getPaddingLeft.replace('px', ''));
  //
  //           const getPaddingRight = window.getComputedStyle(record, null).getPropertyValue('padding-right');
  //           const paddingRight = isNullOrUndefined(getPaddingRight) ? 0 : Number(getPaddingRight.replace('px', ''));
  //
  //           const getMarginLeft = window.getComputedStyle(record, null).getPropertyValue('margin-left');
  //           const marginLeft = isNullOrUndefined(getMarginLeft) ? 0 : Number(getMarginLeft.replace('px', ''));
  //
  //           const getMarginRight = window.getComputedStyle(record, null).getPropertyValue('margin-right');
  //           const marginRight = isNullOrUndefined(getMarginRight) ? 0 : Number(getMarginRight.replace('px', ''));
  //
  //           const getBorderRegex = /^(.*?)\px/;
  //
  //           const getBorderLeft = window
  //             .getComputedStyle(record, null)
  //             .getPropertyValue('border-left')
  //             .match(getBorderRegex);
  //           const borderleft = isNullOrUndefined(getBorderLeft) ? 0 : Number(getBorderLeft[1]);
  //
  //           const getBorderRight = window
  //             .getComputedStyle(record, null)
  //             .getPropertyValue('border-right')
  //             .match(getBorderRegex);
  //           const borderRight = isNullOrUndefined(getBorderRight) ? 0 : Number(getBorderRight[1]);
  //
  //           let finalWidth = record.offsetWidth - (paddingLeft + paddingRight + (marginLeft + marginRight) + (borderleft + borderRight));
  //           if (onlyOneColumnAvailable) {
  //             finalWidth = finalWidth - 2;
  //           }
  //           data.push(finalWidth);
  //         }
  //       }
  //     });
  //     this.storage.setAppData(Constants.DYNAMIC_COLUMN_WIDTH, data);
  //   }
  // }
  //
  // setTableData(tableId, directiveToCall?: ColumnWidthDirective) {
  //   const table = document.getElementById(tableId) as HTMLTableElement;
  //   const tagNamesToExclude: string[] = ['IMG', 'MDB-CHECKBOX'];
  //   const classNamesToExclude: string[] = ['badge'];
  //   if (!isNullOrUndefined(table)) {
  //     const appData = this.storage.getAppData(Constants.DYNAMIC_COLUMN_WIDTH);
  //     if (!isNullOrUndefined(appData) && !_.isEmpty(appData)) {
  //       const requiredTableHeader: Element = table.querySelector('thead tr');
  //       const requiredColumnsRows: NodeListOf<Element> = table.querySelectorAll('tbody tr');
  //       const numberOfColumns = table.rows[0].cells.length;
  //
  //       // Header
  //
  //       for (let i = 0; i < numberOfColumns; i++) {
  //         const columnToApplyStyle: HTMLElement = requiredTableHeader.children[i] as HTMLElement;
  //         if (!columnToApplyStyle.hasAttribute('hidden') && appData[i] !== 0) {
  //           columnToApplyStyle.style.maxWidth = appData[i] + 'px';
  //           columnToApplyStyle.style.minWidth = appData[i] + 'px';
  //           columnToApplyStyle.style.width = appData[i] + 'px';
  //         }
  //       }
  //
  //       // Individual Columns
  //
  //       for (let i = 0; i < numberOfColumns; i++) {
  //         _.forEach(requiredColumnsRows, function(record: HTMLElement) {
  //           let columnToApplyStyle: HTMLElement = record.children[i] as HTMLElement;
  //           if (!isNullOrUndefined(columnToApplyStyle) && !columnToApplyStyle.hasAttribute('hidden') && appData[i] !== 0) {
  //             if (
  //               !_.isEmpty(columnToApplyStyle.children) &&
  //               !isNullOrUndefined(columnToApplyStyle.children[0]) &&
  //               columnToApplyStyle.children[0] instanceof HTMLElement &&
  //               !_.includes(tagNamesToExclude, columnToApplyStyle.children[0].tagName) &&
  //               !columnToApplyStyle.children[0].className.includes('badge')
  //             ) {
  //               columnToApplyStyle = columnToApplyStyle.children[0] as HTMLElement;
  //             }
  //             columnToApplyStyle.style.maxWidth = appData[i] + 'px';
  //             columnToApplyStyle.style.minWidth = appData[i] + 'px';
  //             columnToApplyStyle.style.width = appData[i] + 'px';
  //           }
  //         });
  //       }
  //     }
  //   }
  // }
  //
  // isUserHaveOnlyQCGroupMemberAccess(batch: Batch, employee: Employee): boolean {
  //   return (
  //     this.storage.get(Constants.ROLE_CODE_KEY) !== SystemRole.TRAINING_ADMIN &&
  //     _.some(batch.qcGroupTrainer, { id: employee.empId }) &&
  //     !_.some(batch.coTrainers, { id: employee.empId }) &&
  //     batch.trainer.id !== employee.empId
  //   );
  // }
  //
  // isUserHaveTrainingRoleAccess(batch: Batch, employee: Employee): boolean {
  //   return (
  //     this.storage.get(Constants.ROLE_CODE_KEY) === SystemRole.TRAINING_ADMIN ||
  //     batch.trainer.id === employee.empId ||
  //     _.some(batch.coTrainers, { id: employee.empId })
  //   );
  // }
  //
  // closeDatePicker(datePicker: MDBDatePickerComponent) {
  //   if (!isNullOrUndefined(datePicker)) {
  //     datePicker.closeBtnClicked();
  //   }
  // }
  //
  // getJsonFromStorage(key) {
  //   let storageData = this.storage.get(key);
  //   try {
  //     if (!isNullOrUndefined(storageData) && _.trim(storageData) !== '') {
  //       storageData = window.atob(storageData);
  //       return JSON.parse(storageData);
  //     }
  //   } catch (err) {
  //     return null;
  //   }
  // }
  //
  // getModuleData(requiredModule, indexRequired?) {
  //   const storageJson = this.getJsonFromStorage(Constants.SELECTED_TABLE_SORT);
  //   if (indexRequired) {
  //     return _.findIndex(storageJson, { module: requiredModule });
  //   } else {
  //     return _.find(storageJson, { module: requiredModule });
  //   }
  // }
  //
  // setModuleDataInStorage(module, column, sortOrder) {
  //   let sortStorageData: TableProperty[] = [];
  //   const data = new TableProperty();
  //   data.module = module;
  //   data.columnToSort = column;
  //   data.sortOrder = sortOrder;
  //   const storageData = this.getJsonFromStorage(Constants.SELECTED_TABLE_SORT);
  //   if (!isNullOrUndefined(storageData)) {
  //     const index = this.getModuleData(module, true);
  //     sortStorageData = storageData;
  //     if (index !== -1) {
  //       storageData[index] = data;
  //     } else {
  //       sortStorageData.push(data);
  //     }
  //   } else {
  //     sortStorageData.push(data);
  //   }
  //   this.storage.set(Constants.SELECTED_TABLE_SORT, window.btoa(JSON.stringify(sortStorageData)));
  // }
  //
  // applyFilterFromStorage(filterOptions: FilterOptions, module: string): FilterOptions {
  //   filterOptions.categories = [];
  //   filterOptions.status = null;
  //   const storage = this.storage.get(Constants.SELECTED_COLUMNS_KEY);
  //   if (!isNullOrUndefined(storage) && _.trim(storage) !== '') {
  //     const userData = window.atob(storage);
  //     if (!isNullOrUndefined(userData) && _.trim(userData) !== '') {
  //       const columnData: ColumnFilter[] = JSON.parse(userData);
  //       let requiredColumnData: ColumnFilter = new ColumnFilter();
  //       columnData.forEach(column => {
  //         if (column.module.toLowerCase() === module.toLowerCase()) {
  //           requiredColumnData = column;
  //         }
  //       });
  //       if (isNullOrUndefined(requiredColumnData) || isNullOrUndefined(requiredColumnData.data)) {
  //         return filterOptions;
  //       }
  //       requiredColumnData.data.forEach(data => {
  //         switch (data.column) {
  //           case 'content type':
  //             filterOptions.contentTypes = [];
  //             data.required.forEach(requiredData => {
  //               filterOptions.contentTypes.push(requiredData.content);
  //               switch (requiredData.content) {
  //                 case 'Subscribed':
  //                   filterOptions.subscribedContent = true;
  //                   break;
  //                 case 'My Company':
  //                   filterOptions.ownContent = true;
  //                   break;
  //                 case 'Public':
  //                   filterOptions.publicContent = true;
  //                   break;
  //               }
  //             });
  //             break;
  //           case 'status':
  //             if (data.required[0].content) {
  //               filterOptions.status = true;
  //             } else {
  //               filterOptions.status = false;
  //             }
  //             break;
  //           case 'category':
  //             filterOptions.categories = [];
  //             data.required.forEach(value => {
  //               filterOptions.categories.push(value.content);
  //             });
  //             break;
  //           case 'question type':
  //             filterOptions.questionTypes = [];
  //             data.required.forEach(value => {
  //               filterOptions.questionTypes.push(value.content);
  //             });
  //             break;
  //           case 'created by':
  //             filterOptions.createdName = [];
  //             data.required.forEach(value => {
  //               filterOptions.createdName.push(value.content);
  //             });
  //             break;
  //           case 'tags':
  //             filterOptions.tags = [];
  //             data.required.forEach(value => {
  //               filterOptions.tags.push(value.content);
  //             });
  //             break;
  //           case 'mode':
  //             filterOptions.mode = [];
  //             data.required.forEach(value => {
  //               filterOptions.mode.push(value.content.charAt(0));
  //             });
  //             break;
  //           case 'skill':
  //             filterOptions.skillIds = [];
  //             data.required.forEach(value => {
  //               filterOptions.skillIds.push(value.content.charAt(0));
  //             });
  //             break;
  //           default:
  //             break;
  //         }
  //       });
  //     }
  //   }
  //   return filterOptions;
  // }
  //
  // getInternName(firstName: string, lastName: string) {
  //   let name = '';
  //   if (_.trim(firstName) != '') {
  //     name = firstName;
  //   }
  //   if (_.trim(lastName) != '') {
  //     name += ',' + lastName;
  //   }
  //   return name;
  // }
  //
  // applyDynamicColumnWidth(table: HTMLElement, directive: ColumnWidthDirective, doSetData?: boolean, isTimerRequired?: boolean): void {
  //   const self = this;
  //   if (isTimerRequired) {
  //     const source = timer(TimeoutConstants.TIMEOUT_500);
  //     source.subscribe(val => {
  //       directive.resizeForSpecificTable(table);
  //       if (doSetData) {
  //         self.setTableData(table.getAttribute('id'));
  //       }
  //     });
  //   } else {
  //     directive.resizeForSpecificTable(table);
  //     if (doSetData) {
  //       self.setTableData(table.getAttribute('id'));
  //     }
  //   }
  // }
}
