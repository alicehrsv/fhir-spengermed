import { Routes } from '@angular/router';
import { HomieComponent } from './homie/homie.component';
import { PatientListComponent } from './patient-list/patient-list.component';
import { PatientDetailsComponent } from './patient-details/patient-details.component';
import { ManufacturedItemDefComponent } from './manufactureditemdef-list/manufactureditemdef-list.component';
import { PractitionerListComponent } from './practitioner-list/practitioner-list.component';
import { PractitionerDetailsComponent } from './practitioner-details/practitioner-details.component';
import { EncounterListComponent } from './encounter-list/encounter-list.component';
import { EncounterDetailsComponent } from './encounter-details/encounter-details.component';
import { authGuard } from './auth.guard';
import {MedicationListComponent} from './medication-list/medication-list.component';
import { ObservationDetailsComponent } from './observation-details/observation-details';
import { ObservationListComponent } from './observation-list/observation-list';



export const routes: Routes = [

  {
    path: '',
    title: 'Patienten',
    component: PatientListComponent,     // Falls du PatientsComponent hast → ersetzen
    canActivate: [authGuard]
  },
  {
    path: 'patients',
    component: PatientListComponent,
    canActivate: [authGuard]
  },
  {
    path: 'patient-details/:id',
    component: PatientDetailsComponent,
    canActivate: [authGuard]
  },
  {
    path: 'manufactureditemdefs',
    component: ManufacturedItemDefComponent,
    canActivate: [authGuard]
  },
  {
    path: 'practitioners',
    component: PractitionerListComponent,
    canActivate: [authGuard]
  },
  {
    path: 'practitioner-details/:id',
    component: PractitionerDetailsComponent,
    canActivate: [authGuard]
  },
  {
    path: 'encounters',
    component: EncounterListComponent,
    canActivate: [authGuard]
  },
  {
    path: 'encounter-details/:id',
    component: EncounterDetailsComponent,
    canActivate: [authGuard]
  },
  {
    path: 'encounter-list/:id',
    component: EncounterListComponent,
    canActivate: [authGuard]
  },
{
  path: 'medications',
    component: MedicationListComponent,
  canActivate: [authGuard]
},
/*{
  path: 'medication-details/:id',
    component: MedicationDetailsComponent,
  canActivate: [authGuard]
},
*/
  {
    path: 'observations',
    component: ObservationListComponent,
    canActivate: [authGuard]
  },
  {
    path: 'observation-details/:id',
    component: ObservationDetailsComponent,
    canActivate: [authGuard]
  },




  {
    path: 'plf5a22-list/:id',
    component: PLF2_1ListComponent,
    canActivate: [authGuard]
  },
  {
    path: 'plf5a22-details/:id',
    component: PLF2_1DetailsComponent,
    canActivate: [authGuard]
  }


];
