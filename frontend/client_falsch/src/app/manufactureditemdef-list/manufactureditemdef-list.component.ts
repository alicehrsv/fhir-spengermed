import { Component, OnInit } from '@angular/core';
import { DataService } from '../data.service';
import { ManufacturedItemDefinition } from '../models/ManufacturedItemDef';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-manufactureditemdef-list',
  templateUrl: './manufactureditemdef-list.component.html',
  styleUrls: ['./manufactureditemdef-list.component.scss'],
  standalone: true,
  imports: [CommonModule]
})
export class ManufacturedItemDefComponent implements OnInit {


  public manufactureditemdefs: ManufacturedItemDefinition[] = [];
  public selectedManufacturedItemDef?: ManufacturedItemDefinition;
  public errorMessage: string = '';

  constructor(private dataService: DataService) { }

  ngOnInit(): void {
    this.dataService.getManufacturedItemDef().subscribe({
      next: defs => this.manufactureditemdefs = defs,
      error: err => this.errorMessage = 'Fehler beim Laden!'
    });
  }

  getManufacturedItemDef(): void {
    this.dataService.getManufacturedItemDef()
      .subscribe({
        next: (manufactureditemdefs: ManufacturedItemDefinition[]) => {
          this.manufactureditemdefs = manufactureditemdefs;
          this.errorMessage = '';
        },
        error: (err: any) => {
          this.errorMessage = 'Fehler beim Laden der ManufacturedItemDef!';
          console.error(err);
        }
      });
  }

  public selectManufacturedItemDef(manufactureditemdef: ManufacturedItemDefinition): void {
    console.log(manufactureditemdef);
    this.selectedManufacturedItemDef = manufactureditemdef;
  }
}

function getManufacturedItemDef() {
  throw new Error('Function not implemented.');
}
