import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManufacturedItemDefComponent } from './manufactureditemdef-list.component';

describe('ManufactureditemdefListComponent', () => {
  let component: ManufacturedItemDefComponent;
  let fixture: ComponentFixture<ManufacturedItemDefComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManufacturedItemDefComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManufacturedItemDefComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
