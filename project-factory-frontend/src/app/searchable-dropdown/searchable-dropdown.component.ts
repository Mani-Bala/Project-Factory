import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { isNullOrUndefined } from 'util';
import * as _ from 'lodash';
import { Constants } from '../constant/constants';
import {TimeoutConstants} from '../constant/enums.enum';

@Component({
  selector: 'app-searchable-dropdown',
  templateUrl: './searchable-dropdown.component.html',
  styleUrls: ['./searchable-dropdown.component.css']
})
export class SearchableDropdownComponent implements OnInit {

  @Input() type: string;
  @Input() selectedId?: string;
  @Input() key: string;
  @Input() data: any[];
  @Input() resetDropDown: boolean;
  @Input() placeholder: string;
  @Output() selectEvent = new EventEmitter();
  dataCopy: any[];
  optionIndex: number;
  searchNoResult: boolean;
  searchText: string;
  dropDownId: string;

  ngOnInit() {
    this.searchText = '';
    this.dropDownId = this.type + '-drop-down';
  }

  ngOnChanges(changes: SimpleChanges) {
    this.searchNoResult = false;
    this.dataCopy = this.data;
    this.optionIndex = -1;
    this.searchText = '';
    if (!isNullOrUndefined(this.dropDownId)) {
      const activeEle = document.getElementById(this.dropDownId).querySelector('.active');
      if (!isNullOrUndefined(activeEle)) {
        activeEle.classList.remove('active');
      } else {
        const inactiveHoverEle = document.getElementById(this.dropDownId).querySelector('.in-active-hover');
        if (!isNullOrUndefined(inactiveHoverEle)) {
          inactiveHoverEle.classList.remove('in-active-hover');
        }
      }
      setTimeout(() => {
        const dropdownEle = document.getElementById(this.dropDownId);
        if (!isNullOrUndefined(dropdownEle)) {
          dropdownEle.scrollTop = 0;
          const element = document.getElementById(this.dropDownId + '-searchTxt');
          if (!isNullOrUndefined(element)) {
            element.focus();
          }
        }
      }, TimeoutConstants.TIMEOUT_100);
    }
  }

  stopClosing(e: Event) {
    e.stopPropagation();
  }

  searchDataList(search, event) {
    // Keycode 40,38,13 are used to restrict effects of arrow down , arrow up and enter key respectively
    if (!isNullOrUndefined(event) && !(event.keyCode === 40 || event.keyCode === 38 || event.keyCode === 13)) {
      this.optionIndex = -1;
      const activeEle = document.getElementById(this.dropDownId).querySelector('.active');
      if (!isNullOrUndefined(activeEle)) {
        activeEle.classList.remove('active');
      }
    }
    if (_.trim(search) === '' || isNullOrUndefined(search)) {
      this.dataCopy = this.data;
    } else {
      const searchTxt = _.trim(search.replace(Constants.MULTIPLE_SPACE_REGEX, ' ').toLowerCase());
      if (this.type !== 'batch') {
        this.dataCopy = _.filter(this.data, function(o) {
          return o.name.toLowerCase().includes(searchTxt);
        });
      } else {
        this.dataCopy = _.filter(this.data, function(o) {
          return o.displayName.toLowerCase().includes(searchTxt);
        });
      }
    }

    this.searchNoResult = _.isEmpty(this.dataCopy);
    if (
      !this.searchNoResult &&
      this.dataCopy.length === 1 &&
      (!isNullOrUndefined(event) && !(event.keyCode === 40 || event.keyCode === 38 || event.keyCode === 13))
    ) {
      const resetInterval = setInterval(() => {
        const element = document.getElementById(this.dropDownId + '-option0');
        if (!isNullOrUndefined(element)) {
          if (!element.classList.contains('in-active')) {
            element.classList.add('active');
            this.optionIndex = 0;
          } else {
            this.optionIndex = -1;
          }
          clearInterval(resetInterval);
        }
      }, TimeoutConstants.TIMEOUT_300);
    }
  }

  navigateDown() {
    if (this.optionIndex < this.dataCopy.length - 1) {
      if (this.optionIndex >= 0) {
        const element = document.querySelector(`#` + this.dropDownId + '-option' + this.optionIndex);
        element.classList.remove('active');
        element.classList.remove('in-active-hover');
      }
      this.optionIndex += 1;
      const activeEle = document.getElementById(this.dropDownId + '-option' + this.optionIndex);
      if (activeEle.classList.contains('in-active')) {
        activeEle.classList.add('in-active-hover');
      } else {
        activeEle.classList.add('active');
      }
      const elementOffSetHeight = activeEle.offsetTop;
      if (elementOffSetHeight > 250) {
        const dropdownEle = document.getElementById(this.dropDownId);
        dropdownEle.scrollTop = elementOffSetHeight;
      }
    }
  }

  getOptionDetails() {
    const option = this.dataCopy[this.optionIndex];
    if (!isNullOrUndefined(option)) {
      if (this.type !== 'batch') {
        if (option.isActive) {
          this.selectEvent.emit(option);
        }
      } else {
        this.selectEvent.emit(option);
      }
    }
  }

  navigateUp() {
    if (this.optionIndex < this.dataCopy.length) {
      if (this.optionIndex >= 0) {
        const element = document.querySelector(`#` + this.dropDownId + '-option' + this.optionIndex);
        element.classList.remove('active');
        element.classList.remove('in-active-hover');
      }
      if (this.optionIndex != -1) {
        this.optionIndex -= 1;
      }
      const dropdownEle = document.getElementById(this.dropDownId);
      if (this.optionIndex >= 0) {
        const activeEle = document.getElementById(this.dropDownId + '-option' + this.optionIndex);
        if (activeEle.classList.contains('in-active')) {
          activeEle.classList.add('in-active-hover');
        } else {
          activeEle.classList.add('active');
        }
        dropdownEle.scrollTop = activeEle.offsetTop;
      } else {
        dropdownEle.scrollTop = 0;
      }
    }
  }

  preventArrowInTextField(event) {
    event.preventDefault();
  }

  selectOption(selectedOption: any, e: Event) {
    if (this.type !== 'batch') {
      if (selectedOption.isActive) {
        this.selectEvent.emit(selectedOption);
      } else {
        e.stopPropagation();
      }
    } else {
      this.selectEvent.emit(selectedOption);
    }
  }

}
