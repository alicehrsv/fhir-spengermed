export class Coding {
  public system: string;
  public code: string;
  public display?: string;

  constructor(system: string = '', code: string = '', display: string = '') {
    this.system = system;
    this.code = code;
    this.display = display;
  }
}
