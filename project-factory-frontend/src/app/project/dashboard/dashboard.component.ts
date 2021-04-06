import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  ElementRef,
  HostListener,
  OnInit,
  ViewChild
} from '@angular/core';
import {NavigationEnd, Router} from '@angular/router';
import {Title} from '@angular/platform-browser';
import {PaginationUtilsService} from '../../services/utils/pagination-utils.service';
import {HttpService} from '../../services/http/http.service';
import {AppUtilsService} from '../../services/utils/app-utils.service';
import {Project} from '../../models/project';
import {Pagination} from '../../models/pagination';
import {isNullOrUndefined} from 'util';
import {MdbTableDirective, MdbTablePaginationComponent} from 'angular-bootstrap-md';
import {GET_PROJECTS, PROJECT_BY_ID} from '../../constant/rest-endpoints';
import {HttpHeaders} from '@angular/common/http';
import {ToastrService} from 'ngx-toastr';
import {NgxSpinnerService} from 'ngx-spinner';
import {Constants} from '../../constant/constants';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit, AfterViewInit {
  projects: Project[];
  selectedProject: Project;
  isActivateTriggered: boolean;
  isDeleteTriggered: boolean;
  @ViewChild('deleteConfirmModal', { static: true })
  deleteModal;
  @ViewChild('activateModal', { static: true })
  activateModal;
  @ViewChild('projectInfoModal', { static: true })
  projectInfoModal;
  pagination: Pagination;
  BASE_HREF = Constants.BASE_URL;
  isProjectsEmpty: boolean;

  @ViewChild(MdbTableDirective, { static: true }) mdbTable: MdbTableDirective;
  @ViewChild(MdbTablePaginationComponent, { static: true }) mdbTablePagination: MdbTablePaginationComponent;
  @ViewChild('row', { static: true }) row: ElementRef;

  elements: any = [];
  headTitles = ['PROJECT NAME', 'CATEGORY', 'CREATED BY', 'STATUS'];
  headElements = ['name', 'categoryName', 'employeeName', 'isActive'];

  searchText: string;
  previous: string;
  maxVisibleItems: number;

  constructor(
    private router: Router,
    private titleService: Title,
    private http: HttpService,
    private paginationUtils: PaginationUtilsService,
    private appUtils: AppUtilsService,
    private cdRef: ChangeDetectorRef,
    private toaster: ToastrService,
    private spinner: NgxSpinnerService
  ) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.titleService.setTitle('Project Factory | Projects');
      }
    });
  }

  @HostListener('input') oninput() {
    this.mdbTablePagination.searchText = this.searchText;
  }

  ngOnInit() {
    this.spinner.show();
    this.pagination = new Pagination();
    this.searchText = '';
    this.maxVisibleItems = 25;
    this.getProjects();
  }

  setProjectInfo(project: Project) {
    this.selectedProject = project;
  }

  gotoProjectCreation() {
    this.router.navigate(['project/create']);
  }

  ngAfterViewInit() {
   this.setMaxVisibleItems();
  }

  setMaxVisibleItems() {
    this.mdbTablePagination.setMaxVisibleItemsNumberTo(this.maxVisibleItems);
    this.mdbTablePagination.calculateFirstItemIndex();
    this.mdbTablePagination.calculateLastItemIndex();
    this.cdRef.detectChanges();
  }

  searchItems() {
    const prev = this.mdbTable.getDataSource();

    if (!this.searchText) {
      this.mdbTable.setDataSource(this.previous);
      this.elements = this.mdbTable.getDataSource();
    }

    if (this.searchText) {
      this.elements = this.mdbTable.searchLocalDataBy(this.searchText);
      this.mdbTable.setDataSource(prev);
    }

    this.mdbTablePagination.calculateFirstItemIndex();
    this.mdbTablePagination.calculateLastItemIndex();

    this.mdbTable.searchDataObservable(this.searchText).subscribe(() => {
      this.mdbTablePagination.calculateFirstItemIndex();
      this.mdbTablePagination.calculateLastItemIndex();
    });
  }

  selectNumberOfItems(num: number) {
    this.pagination = new Pagination();
    this.pagination.itemsPerPage = num;
    this.maxVisibleItems = num;
    this.setMaxVisibleItems();
  }

  getProjects() {
    this.elements = [];
    this.http.get(GET_PROJECTS).subscribe(response => {
      if (!isNullOrUndefined(response.body.data)) {
        for (const data in response.body.data) {
          this.elements.push(response.body.data[data]);
          this.isProjectsEmpty = false;
        }
        this.setTableData();
      } else {
        this.isProjectsEmpty = true;
      }
  });
    this.spinner.hide();
  }

  setTableData() {
    this.mdbTable.setDataSource(this.elements);
    this.elements = this.mdbTable.getDataSource();
    this.previous = this.mdbTable.getDataSource();
  }

  activateDeactivateProject(id: number, status: boolean) {
    this.spinner.show();
    this.isActivateTriggered = true;
    this.http.patch(PROJECT_BY_ID.replace('{projectId}', id.toString()) + '?status=' + !status, null ).subscribe(response => {
      this.activateModal.hide();
      this.getProjects();
      if (!status) {
       this.toaster.success('', 'Project activated successfully');
      } else {
        this.toaster.success('', 'Project deactivated successfully');
      }
      this.isActivateTriggered = false;
    }, err => {
      this.activateModal.hide();
      if (!status) {
        this.toaster.error('', 'Project activate failed');
      } else {
        this.toaster.error('', 'Project deactivate failed');
      }
      this.isActivateTriggered = false;
    }
  );
    this.spinner.hide();
  }

  deleteProject() {
    this.isDeleteTriggered = true;
    this.http.delete(PROJECT_BY_ID.replace('{projectId}', this.selectedProject.id.toString())).subscribe(
      response => {
          this.deleteModal.hide();
          this.toaster.success('', 'Project deleted successfully');
          this.isDeleteTriggered = false;
          this.getProjects();
      },
      err => {
        const errorMsg = isNullOrUndefined(err.error.description) ? 'Project deletion failed' : err.error.description;
        this.deleteModal.hide();
        this.toaster.error('', errorMsg);
        this.getProjects();
        this.isDeleteTriggered = false;
      }
    );
  }

  resetProject() {
    this.selectedProject = null;
  }

  getProjectInfo(id: number) {
    this.http.get(PROJECT_BY_ID.replace('{projectId}', id.toString())).subscribe(
      response => {
      this.selectedProject = response.body.data;
      },
      err => {
        const errorMsg = isNullOrUndefined(err.error.description) ? 'Unable to get project details' : err.error.description;
        this.projectInfoModal.hide();
        this.toaster.error('', errorMsg);
      }
    );
  }
}
