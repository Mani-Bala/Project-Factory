
export class FilterOptions {
  size: number;
  page: number;
  sortOrder: string;
  orderBy: string;
  searchKey: string;
  tags: String[];
  types: any[];
  tracks: string[];
  empIds: number[];
  locations: string[];
  roomNames: string[];
  curriculumNames: string[];
  searchList: String[];
  ids: number[];
  selectedProjIds: number[];
  showMine: boolean;
  isNeedSkills: boolean;

  /** Audit View START */
  ActMovedToAnotherWeek: boolean;
  newAct: boolean;
  completedAct: boolean;
  missedAct: boolean;
  deletedAct: boolean;
  ActMovedToThisWeek: boolean;
  /** Audit View END */

  /** Question  START*/
  questionTypes: string[];
  status: Boolean;
  categories: string[];
  /** Question  END*/

  /** Quiz */
  mode: string[];
  createdName: string[];
  // questionsToDelete: Question[];
  /** Quiz END */

  /** Video START */
  urls: string[];
  levels: string[];
  createdBy: string[];
  /** Video END */

  /** Content type  START*/
  subscribedContent = false;
  publicContent = false;
  ownContent = false;
  /** Content type  END*/

  contentTypes: string[];
  skillIds: string[];
  projectIdsToSkip: number[];

  /** Curriculum Activity */
  activityStatus: string[];
}
