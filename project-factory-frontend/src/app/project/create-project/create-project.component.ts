import {Component, OnInit, ViewChild} from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Title } from '@angular/platform-browser';
import * as _ from 'lodash';
import {Skill} from '../../models/skill';
import { isNullOrUndefined } from 'util';
import {MessageRenderer} from '../../models/message-renderer';
import {Project} from '../../models/project';
import {Category} from '../../models/category';
import { Observable, Subject } from 'rxjs';
import { startWith, map } from 'rxjs/operators';
import {GET_PROJECT, GET_PROJECT_METADATA} from '../../constant/rest-endpoints';
import {NgxSpinnerService} from 'ngx-spinner';
import {HttpService} from '../../services/http/http.service';
import {ToastrService} from 'ngx-toastr';
import {ProjectSkill} from '../../models/project-skill';
import {HttpStatus} from '../../constant/enums.enum';

@Component({
  selector: 'app-create-project',
  templateUrl: './create-project.component.html',
  styleUrls: ['./create-project.component.css']
})
export class CreateProjectComponent implements OnInit {
  projectId: number;
  isSaveTriggered: boolean;
  isDeleteTriggered: boolean;
  @ViewChild('deleteConfirmModal', { static: true })
  deleteConfirmModal;
  selectedSkills: Skill[];
  removedSkills: Skill[];
  skillToRemove: Skill;
  skillSearch: string;
  dataService: string[];
  pageFlag: string;
  message: MessageRenderer;
  skillMessage: boolean;
  isProjectNameRequired: boolean;
  isCategoryRequired: boolean;
  isSkillRequired: boolean;
  isDataChanged: boolean;
  project: Project;
  submitted: boolean;
  selectedCategory: Category;
  categories: Category[];
  resetDropdown: boolean;
  searchText = new Subject();
  results: Observable<string[]>;
  skills: Skill[];
  skillColName: string;
  skillColSortType: boolean | 'desc' | 'asc';
  isCategoriesEmpty: boolean;

  constructor( private activatedRoute: ActivatedRoute,
               private router: Router,
               private titleService: Title,
               private http: HttpService,
               private toaster: ToastrService,
               private spinner: NgxSpinnerService) { }

  ngOnInit() {
    const id = this.activatedRoute.snapshot.paramMap.get('id');
    this.message = new MessageRenderer();
    this.project = new Project();
    this.categories = new Array();
    this.selectedSkills = new Array();
    this.removedSkills = new Array();
    this.projectId = parseInt(id, 0);
    this.getProjectMetadata();
    this.resetDropdown = true;
  }

  removeSelectedSkills() {
    _.remove(this.selectedSkills, this.skillToRemove);
    if (this.pageFlag !== 'create' && this.skillToRemove.isAlreadySelected) {
      this.removedSkills.push(this.skillToRemove);
    }
    this.skills.push(this.skillToRemove);
    this.deleteConfirmModal.hide();
    this.updateSelectedSkills(this.skillToRemove);
  }

  updateSelectedSkills(skill?: Skill) {
    if (!isNullOrUndefined(this.selectedSkills) && !isNullOrUndefined(this.dataService)) {
      for (const data of this.selectedSkills) {
        _.remove(this.dataService, content => {
          return content === data.name;
        });
      }
    }
    if (!isNullOrUndefined(skill) && !isNullOrUndefined(this.dataService) && !this.dataService.includes(skill.name)) {
      this.dataService.push(skill.name);
    }
    this.updateResultsData();
  }

  updateResultsData() {
    this.results = this.searchText.pipe(
      startWith(''),
      map((value: string) => this.filter(value))
    );
    this.skillSearch = '';
    const ele = document.getElementById('crtCurrTag') as HTMLInputElement;
    if (!isNullOrUndefined(ele)) {
      ele.value = '';
    }
  }

  filter(value: string) {
    if (!isNullOrUndefined(value)) {
      const filterValue = value.toLowerCase();
      return this.dataService.filter((item: string) => item.toLowerCase().includes(filterValue));
    }
  }

  submitProject() {
    this.isSaveTriggered = true;
    this.submitted = true;
    if (this.validateProject()) {
      this.project.name = _.trim(this.project.name);
      this.project.category = this.selectedCategory;
      if (!_.isEmpty(this.selectedSkills)) {
        const projectSkills: ProjectSkill[] = new Array();
        _.forEach(this.selectedSkills, function(skill) {
          const projectSkill = new ProjectSkill();
          projectSkill.skill = skill.id;
          projectSkill.score = skill.score;
          projectSkills.push(projectSkill);
        });
        this.project.projectSkills = projectSkills;
      }
      if (!_.isEmpty(this.removedSkills)) {
        const removedSkills: ProjectSkill[] = new Array();
        _.forEach(this.removedSkills, function(skill) {
          const internshipSkill = new ProjectSkill();
          internshipSkill.skill = skill.id;
          removedSkills.push(internshipSkill);
        });
        this.project.removedSkills = removedSkills;
      }
      this.http.post(GET_PROJECT, this.project).subscribe(
        response => {
          if (!isNullOrUndefined(response.body)) {
            this.toaster.success('', 'Project saved successfully. Redirecting to dashboard..');
            setTimeout(
              function() {
                this.goBack();
              }.bind(this),
              1500
            );
          }
        },
        error => {
          this.isSaveTriggered = false;
          if (error.status === HttpStatus.CONFLICT) {
            this.toaster.error('', error.error.description);
          } else if (error.status === HttpStatus.UNPROCESSABLE_ENTITY) {
            this.toaster.error('', error.error.description);
          } else {
            this.toaster.error('', 'Project save failed');
          }
        }
      );
    } else {
      this.isSaveTriggered = false;
    }
  }

  validateProject() {
    let isValidData = true;
    if (isNullOrUndefined(this.project.name) || _.trim(this.project.name) === '') {
      this.isProjectNameRequired = true;
      isValidData = false;
    }
    if (isNullOrUndefined(this.selectedCategory)) {
      this.isCategoryRequired = true;
      isValidData = false;
    }
    if ((isNullOrUndefined(this.selectedSkills) || _.isEmpty(this.selectedSkills))) {
      this.isSkillRequired = true;
      isValidData = false;
    }
    return isValidData;
  }

  updateIsDataChanged() {
    if (!isNullOrUndefined(this.project.name) && _.trim(this.project.name) !== '') {
      this.isProjectNameRequired = false;
    }
    this.isDataChanged = true;
  }

  selectCategory(data: any) {
    this.isCategoryRequired = false;
    this.selectedCategory = data;
    this.updateIsDataChanged();
  }

  onSkillSelected(skill) {
    this.isSkillRequired = false;
    const enteredSkill = _.filter(this.skills, { name: skill.name });
    if (!isNullOrUndefined(skill) && !_.isEmpty(enteredSkill)) {
      const foundSkill = _.find(this.selectedSkills, { id: enteredSkill[0].id });
      if (_.isEmpty(foundSkill)) {
        enteredSkill[0].isAlreadySelected = false;
        enteredSkill[0].score = enteredSkill[0].oldScore;
        this.selectedSkills.push(enteredSkill[0]);
        if (!_.isEmpty(this.removedSkills)) {
          _.remove(this.removedSkills, skill => {
            return skill.id === enteredSkill[0].id;
          });
        }
        _.remove(this.skills, skill => {
          return skill.id === enteredSkill[0].id;
        });
        _.pull(this.dataService, skill);
        this.updateResultsData();
        this.skillSearch = '';
        this.skillMessage = false;
        this.message = new MessageRenderer();
      } else {
        this.skillMessage = true;
        this.message.message = 'Skill has been added already';
        this.message.type = 'ERROR';
      }
    }
  }

  checkValidScore(skill: Skill) {
    if (isNullOrUndefined(skill.score) || skill.score <= 0 || skill.score > 999) {
      skill.score = skill.oldScore;
    }
  }

  restrictToMaxLength(ele, skill) {
    let val = ele.srcElement.value;
    if (val.includes('.')) {
      val = Math.trunc(val);
      skill.score = val;
    }
    if (val.length > Number(ele.srcElement.attributes.maxlength.value)) {
      val = val.slice(0, 3);
      ele.srcElement.value = val;
      skill.score = val;
    }
  }

  setSkillToRemove(skill: Skill) {
    this.skillToRemove = skill;
  }

  sortSkills(columnName: string) {
    if (!isNullOrUndefined(this.skillColName) && this.skillColName !== columnName) {
      this.skillColSortType = null;
    }
    this.skillColName = columnName;
    this.skillColSortType = isNullOrUndefined(this.skillColSortType) || this.skillColSortType === 'desc' ? 'asc' : 'desc';
    if (this.skillColName === 'name') {
      this.selectedSkills = _.orderBy(
        this.selectedSkills,
        [(skill: Skill) => (skill.name ? skill.name.toLowerCase() : '')],
        [this.skillColSortType]
      );
    } else {
      this.selectedSkills = _.orderBy(this.selectedSkills, [columnName], [this.skillColSortType]);
    }
  }

  goBack() {
    this.router.navigate(['project/dashboard']);
  }

  getProjectMetadata() {
    this.http.get(GET_PROJECT_METADATA).subscribe(response => {
      if (!isNullOrUndefined(response.body.data)) {
        this.categories = response.body.data.categoryDTOS;
        this.skills = response.body.data.skillDTOS;
        this.setSkillsData();
        if (_.isEmpty(this.categories)) {
          this.isCategoriesEmpty = true;
        } else {
          this.isCategoriesEmpty = false;
        }
      } else {
        this.isCategoriesEmpty = true;
      }
    });
    this.spinner.hide();
  }

  setSkillsData() {
    if (!_.isEmpty(this.skills)) {
      this.skills.forEach(skill => {
      skill.oldScore = skill.score;
    });
      this.dataService = _.map(this.skills, 'name');
      this.updateSelectedSkills();
    }
  }

  clearProject() {
    this.project = new Project();
    this.selectedCategory = new Category();
    if (!_.isEmpty(this.selectedSkills)) {
      this.skills = this.skills.concat(this.selectedSkills);
    }
    this.selectedSkills = [];
  }
}
