import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import {Patient} from './models/Patient';
import {ManufacturedItemDefinition} from './models/ManufacturedItemDef';
import {Practitioner} from './models/Practitioner';
import {Encounter} from './models/Encounter';
import {Medication} from './models/Medication';
import {Bundle} from './models/Bundle';
import { Observation } from './models/Observation';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  // Alle URLs auf relative Pfade ändern
  private patienturl: string = "/api/patient";
  private manufactureditemdefsurl: string = "/api/manufactureditemdef";
  private practitionerUrl: string = "/api/practitioner";
  private encounterUrl: string = "/api/encounter";
  private readonly medicationUrl = "/api/medication";
  private readonly observationUrl = "/api/observation";
  private readonly PLF5A22Url = "/api/PLF5A22";

  constructor(private httpclient: HttpClient) {}

  // Patienten
  public getPatients(): Observable<Patient[]> {
    return this.httpclient.get<Patient[]>(this.patienturl);
  }

  public getPatient(id: string): Observable<Patient> {
    return this.httpclient.get<Patient>(`${this.patienturl}/${id}`);
  }

  public savePatient(patient: Patient): Observable<Patient> {
    return this.httpclient.put<Patient>(`${this.patienturl}/${patient.id}`, patient);
  }

  public addPatient(patient: Patient): Observable<Patient> {
    return this.httpclient.post<Patient>(this.patienturl, patient);
  }

  public deletePatient(id: string): Observable<Patient> {
    return this.httpclient.delete<Patient>(`${this.patienturl}/${id}`);
  }

  // ManufacturedItemDefinition
  public getManufacturedItemDef(): Observable<ManufacturedItemDefinition[]> {
    return this.httpclient.get<ManufacturedItemDefinition[]>(this.manufactureditemdefsurl);
  }

  // Practitioners
  public getPractitioners(id?: string): Observable<Practitioner[]> {
    if (id && id.trim() !== '') return this.httpclient.get<Practitioner[]>(`${this.practitionerUrl}/${id}`);
    // Default: get without params returns a list (Bundle -> map to entries)
    return this.httpclient.get<any>(this.practitionerUrl).pipe(
      map(b => b && b.entry ? b.entry.map((e: any) => e.resource) : [])
    );
  }

  public searchPractitioners(params: {[key: string]: any} = {}): Observable<{total:number, practitioners: Practitioner[]}> {
    let httpParams = new HttpParams();
    Object.keys(params).forEach(k => { if (params[k] !== undefined && params[k] !== null) httpParams = httpParams.set(k, String(params[k])); });
    return this.httpclient.get<any>(this.practitionerUrl, { params: httpParams }).pipe(
      map(b => ({ total: b.total || 0, practitioners: b.entry ? b.entry.map((e: any) => e.resource) : [] }))
    );
  }

  public updatePractitioner(practitioner: Practitioner): Observable<Practitioner> {
    return this.httpclient.put<Practitioner>(`${this.practitionerUrl}/${practitioner.id}`, practitioner);
  }

  public deletePractitioner(id: string): Observable<void> {
    return this.httpclient.delete<void>(`${this.practitionerUrl}/${id}`);
  }

  public addPractitioner(practitioner: Practitioner): Observable<Practitioner> {
    return this.httpclient.post<Practitioner>(this.practitionerUrl, practitioner);
  }

  // Encounters (lokal)
  private encounters: Encounter[] = [];

  getEncounters(): Observable<Encounter[]> {
    return of(this.encounters);
  }

  addEncounter(enc: Encounter): Observable<void> {
    this.encounters.push(enc);
    return of();
  }

  updateEncounter(enc: Encounter): Observable<void> {
    const idx = this.encounters.findIndex(e => e.id === enc.id);
    if (idx >= 0) this.encounters[idx] = enc;
    return of();
  }

  // Medications
  public getMedications(): Observable<Medication[]> {
    return this.httpclient.get<Medication[]>(`${this.medicationUrl}`);
  }

  public getMedication(id: string): Observable<Medication> {
    return this.httpclient.get<Medication>(`${this.medicationUrl}/${id}`);
  }

  public addMedication(medication: Medication): Observable<Medication> {
    return this.httpclient.post<Medication>(`${this.medicationUrl}`, medication);
  }

  public updateMedication(id: string, medication: Medication): Observable<Medication> {
    return this.httpclient.put<Medication>(`${this.medicationUrl}/${id}`, medication);
  }

  public deleteMedication(id: string): Observable<void> {
    return this.httpclient.delete<void>(`${this.medicationUrl}/${id}`);
  }

  public saveMedication(medication: Medication): Observable<Medication> {
    if (medication.id && medication.id.trim() !== '') {
      return this.updateMedication(medication.id, medication);
    }
    return this.addMedication(medication);
  }

  public searchMedications(params: any): Observable<Bundle> {
    const query = Object.keys(params)
      .filter(k => params[k] !== '' && params[k] !== null)
      .map(k => `${k}=${encodeURIComponent(params[k])}`)
      .join('&');

    return this.httpclient.get<Bundle>(
      `${this.medicationUrl}?${query}`
    );
  }

  // Patient Search
  searchPatients(params: any) {
    const query = Object.keys(params)
      .filter(k => params[k] !== '' && params[k] !== null)
      .map(k => `${k}=${encodeURIComponent(params[k])}`)
      .join('&');

    return this.httpclient.get<Bundle>(
      `${this.patienturl}?${query}`
    );
  }

  // ==================== OBSERVATION METHODS ====================

  /**
   * Get all observations
   */
  public getObservations(): Observable<Observation[]> {
    return this.httpclient.get<Observation[]>(this.observationUrl);
  }

  /**
   * Get single observation by ID
   */
  public getObservation(id: string): Observable<Observation> {
    return this.httpclient.get<Observation>(`${this.observationUrl}/${id}`);
  }

  /**
   * Create new observation
   */
  public addObservation(observation: Observation): Observable<Observation> {
    return this.httpclient.post<Observation>(this.observationUrl, observation);
  }

  /**
   * Update existing observation
   */
  public updateObservation(id: string, observation: Observation): Observable<Observation> {
    return this.httpclient.put<Observation>(`${this.observationUrl}/${id}`, observation);
  }

  /**
   * Delete single observation by ID
   */
  public deleteObservation(id: string): Observable<void> {
    return this.httpclient.delete<void>(`${this.observationUrl}/${id}`);
  }

  /**
   * Delete ALL observations (use with extreme caution!)
   */
  public deleteAllObservations(): Observable<void> {
    return this.httpclient.delete<void>(this.observationUrl);
  }

  /**
   * Save observation (create or update)
   */
  public saveObservation(observation: Observation): Observable<Observation> {
    if (observation.id && observation.id.trim() !== '') {
      return this.updateObservation(observation.id, observation);
    }
    return this.addObservation(observation);
  }

  /**
   * Search observations with parameters
   */
  public searchObservations(params: any): Observable<Bundle> {
    const query = Object.keys(params)
      .filter(k => params[k] !== '' && params[k] !== null)
      .map(k => `${k}=${encodeURIComponent(params[k])}`)
      .join('&');

    return this.httpclient.get<Bundle>(
      `${this.observationUrl}?${query}`
    );
  }





  /**
   * Get all observations
   */
  public getPLF5A22(): Observable<PLF5A22[]> {
    return this.httpclient.get<PLF5A22[]>(this.PLF5A22Url);
  }

  /**
   * Get single PLF5A22 by ID
   */
  public getPLF5A22(id: string): Observable<PLF5A22> {
    return this.httpclient.get<PLF5A22>(`${this.PLF5A22Url}/${id}`);
  }

  /**
   * Create new PLF5A22
   */
  public addPLF5A22(PLF5A22: PLF5A22): Observable<PLF5A22> {
    return this.httpclient.post<PLF5A22>(this.PLF5A22Url, PLF5A22);
  }

  /**
   * Update existing PLF5A22
   */
  public updatePLF5A22(id: string, PLF5A22: PLF5A22): Observable<PLF5A22> {
    return this.httpclient.put<PLF5A22>(`${this.PLF5A22Url}/${id}`, PLF5A22);
  }

  /**
   * Delete single PLF5A22 by ID
   */
  public deletePLF5A22(id: string): Observable<void> {
    return this.httpclient.delete<void>(`${this.PLF5A22Url}/${id}`);
  }

  /**
   * Delete ALL PLF5A22s (use with extreme caution!)
   */
  public deleteAllPLF5A22s(): Observable<void> {
    return this.httpclient.delete<void>(this.PLF5A22Url);
  }

  /**
   * Save PLF5A22 (create or update)
   */
  public savePLF5A22(PLF5A22: PLF5A22): Observable<PLF5A22> {
    if (PLF5A22.id && PLF5A22.id.trim() !== '') {
      return this.updatePLF5A22(PLF5A22.id, PLF5A22);
    }
    return this.addPLF5A22(PLF5A22);
  }

  /**
   * Search PLF5A22s with parameters
   */
  public searchPLF5A22s(params: any): Observable<Bundle> {
    const query = Object.keys(params)
      .filter(k => params[k] !== '' && params[k] !== null)
      .map(k => `${k}=${encodeURIComponent(params[k])}`)
      .join('&');

    return this.httpclient.get<Bundle>(
      `${this.PLF5A22Url}?${query}`
    );
  }


}
