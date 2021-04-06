import {Employee} from './employee';
import {Category} from './category';
import {ProjectSkill} from './project-skill';

export class Project {
  id: number;
  name: string;
  description: string;
  skills: string;
  isActive: boolean;
  createdBy: Employee;
  createdByName: string;
  category: Category;
  categoryName: string;
  projectSkills: ProjectSkill[];
  removedSkills: ProjectSkill[];
  showAllSkills: boolean;
  contentType: string;
  totalRecords: number;
}
