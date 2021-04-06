export class IndividualColumnFilter {
  data: string;
  isRequired: boolean;
  isFilterApplied: boolean;
  ids: string[];
  id: string;

  setIsRequired(isRequired: boolean): this {
    this.isRequired = true;
    return this;
  }
}
