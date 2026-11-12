package at.spengergasse.spengerfhirmed.model;

/**
 * Enum für die Property-Codes des CodeSystem.
 * Repräsentiert die Spalten der ICPC2_SNOMED_ICD10 Tabelle,
 * die als concept.property gespeichert werden.
 *
 * HINWEIS: Die 8 Synonym-Spalten werden NICHT hier abgebildet –
 * sie werden als concept.designation (CodeSystemConceptDesignation) gespeichert.
 */
public enum CodeCode {
    SNOMEDCTID,        // Tatsächliche SNOMED CT ID (Spalte "SnomedCT ID")
    SNOMEDCTBEGRIFFE,  // SNOMED CT Bezeichnung (Spalte "FSN-SnomedCT-International-Browser")
    ICPC2ID,           // ICPC2 Code (Spalte "ICPC2 ID")
    KRITERIEN,         // Kriterien (Spalte "Kriterien")
    HINWEISE,          // Hinweise (Spalte "Hinweise")
    FARBE,             // Farbe als Integer (Spalte "Farbe")
    KOMPONENTE,        // Komponente als Integer (Spalte "Komponente")
    ICD10BEGRIFFE      // ICD-10 Begriff (Spalte "ICD-10-Begriffe")
}