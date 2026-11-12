export class Quantity {
  public id?: string;
  public value: number | null = null;
  public unit: string = '';
  public system: string = '';
  public code: string = '';

  constructor(init?: Partial<Quantity>) {
    this.id = init?.id;
    this.value = typeof init?.value === 'number' ? init.value : null;
    this.unit = init?.unit ?? '';
    this.system = init?.system ?? '';
    this.code = init?.code ?? '';
  }
}
