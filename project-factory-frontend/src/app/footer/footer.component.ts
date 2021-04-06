import { Component, OnInit } from '@angular/core';
import {Constants, UNAUTHERIZED_PATH} from "../constant/constants";

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent implements OnInit {
  currentYear = new Date();
  appName: string;
  constructor() { }

  ngOnInit() {
    this.appName = Constants.DEFAULT_APPNAME;
  }

  checkIfCurrentPathIsInUnAuthLocation() {
    let path = window.location.pathname;
    return UNAUTHERIZED_PATH.includes(path);
  }
}
