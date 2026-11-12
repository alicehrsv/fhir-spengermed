-- ========================
-- 1. Parent Tables
-- ========================

-- Patienten
INSERT INTO pa_patient (id, p_active, p_gender, p_birthdate, p_deceasedboolean, p_deceaseddatetime) VALUES
                                                                                                        ('098', true, 'female', NULL, false, NULL),
                                                                                                        ('fed', true, 'male', NULL, false, NULL),
                                                                                                        ('0982fs', false, 'unknown', NULL, true, '2035-12-31'),
                                                                                                        ('918', true, 'male', '1990-01-01', false, NULL),
                                                                                                        ('4321', true, 'female', '1985-05-15', false, NULL),
                                                                                                        ('859023f', true, 'other', '2000-12-31', false, NULL);

-- Organization
INSERT INTO or_organization (id, o_active, o_name) VALUES
    (1, true, 'Blijdorp Medisch Centrum (BUMC)');

-- ========================
-- 2. 1:1 Relationships (Perioden)
-- ========================

INSERT INTO pe_period (id, pe_start, pe_end) VALUES
                                                 ('789xyz', '2000-01-01', '2035-12-31'),
                                                 ('34gh', '2000-01-01', '2035-12-31'),
                                                 ('5678gh', '2000-01-01', '2035-12-31');

-- ========================
-- 3. Identifiers
-- ========================

INSERT INTO i_identifier (id, i_use, i_system, i_value, i_p_id) VALUES
                                                                    ('456abc', 'official', 'urn:oid:1.2.36.146.595.217.0.2', '67890', '098'),
                                                                    ('p001', 'official', 'urn:oid:1.2.826.0.1.3680043.8.493', '890123456', 'fed'),
                                                                    ('p002', 'usual', 'urn:oid:1.2.840.1.113883.3.445.2.1', 'ABC123XYZ', '0982fs'),
                                                                    ('id1', 'usual', 'urn:oid:1.2.36.146.595.217.0.1', '12345', '918'),
                                                                    ('id2', 'official', 'urn:oid:1.2.36.146.595.217.0.1', '54321', '4321');

-- ========================
-- 4. Human Names
-- ========================

INSERT INTO hn_human_name (id, hn_use, hn_family, hn_text, hn_p_id) VALUES
                                                                        ('456def', 'official', 'Müller', NULL, '098'),
                                                                        ('789ghi', 'usual', 'Müller', NULL, 'fed'),
                                                                        ('hn01', 'official', 'Smith', NULL, '0982fs'),
                                                                        ('hn1', 'official', 'Smith', 'John Smith', '918'),
                                                                        ('hn2', 'official', 'Doe', 'Jane Doe', '4321'),
                                                                        ('hn3', 'official', 'Taylor', 'Alex Taylor', '859023f');


INSERT INTO hn_given (hn_id, given) VALUES
                                        ('789ghi','Anna'), ('456def','Maria');


INSERT INTO hn_prefix (hn_id, prefix) VALUES
                                          ('789ghi','Prof.'), ('789ghi','Dr.'), ('789ghi','Dipl.-Ing.'), ('789ghi','Mag.'),
                                          ('hn01','Mr.'), ('hn2','Ms.'), ('hn3','Mx.');

INSERT INTO hn_suffix (hn_id, suffix) VALUES
                                          ('789ghi','PhD'), ('789ghi','MBA'), ('p001','PhD'), ('hn1','Jr.');

-- ========================
-- 5. Contact Points
-- ========================

INSERT INTO cp_contactpoint (id, cp_system, cp_value, cp_use, cp_ranke, c_p_id, cp_pe_id) VALUES
                                                                                              ('456JK','phone','0123456789','home',1,'098','789xyz'),
                                                                                              ('JKL789','email','anna.muller@example.com','home',2,'fed','34gh'),
                                                                                              ('p001','phone','0301234567','work',1,'p001','5678gh'),
                                                                                              ('p002','email','smith.j@example.com','work',1,'0982fs',NULL),
                                                                                              ('p003','fax','0307654321','home',1,'918',NULL),
                                                                                              ('cp1','phone','+43 660 1234567','mobile',1,'4321',NULL),
                                                                                              ('cp2','email','john.smith@example.com','home',2,'859023f',NULL);


-- ========================
-- 6. Addresses
-- ========================

INSERT INTO a_adress (id, a_city, a_country, a_district, a_postalcode, a_state, a_type, a_use, a_pe_id, a_p_id) VALUES
                                                                                                                    ('789jkl','Graz','Österreich','Graz','8010','Steiermark','both','work','789xyz','098'),
                                                                                                                    ('addr1','Vienna','Austria','District 1','1010','Vienna','both','home','34gh','fed'),
                                                                                                                    ('addr2','Vienna','Austria','District 2','1020','Vienna','postal','home','5678gh','0982fs');



INSERT INTO a_address_line (adress_id, line) VALUES
                                                 ('789jkl','Anna Müller'), ('addr1','Hauptstraße 1'), ('addr2','8010 Graz');



-- ========================
-- 7. Attachments
-- ========================

INSERT INTO at_attachment (id, at_content_type, at_data, at_creation, at_height, at_width, at_size, at_pa_id, at_pr_id) VALUES
                                                                                                                            ('789mno', 'image/jpeg', 'data:image/jpeg;base64,/9j/4AA...', '2020-01-01', 100, 100, 10000, '789xyz', NULL),
                                                                                                                            ('at1','application/pdf','JVBERi0xLjUKJcfs...',NULL,NULL,NULL,NULL,NULL,'p001');

-- ========================
-- 8. Practitioner, Qualifications, Narrative
-- ========================

INSERT INTO na_narrative (id, n_status, n_div) VALUES
                                                   ('123','empty','<div xmlns="http://www.w3.org/1999/xhtml">Dr. Anna Müller</div>'),
                                                   ('plf','generated','<div xmlns="http://www.w3.org/1999/xhtml">...</div>');

INSERT INTO pr_practioner (id, pr_gender, pr_birthdate, dr_na_id) VALUES
    ('p001','male','1982-06-15','123');

INSERT INTO q_qualification (id, qu_pr_id, co_qu_id) VALUES
    ('5678','p001','plf4');

-- ========================
-- 9. Manufactured Item
-- ========================

INSERT INTO cc_codeableconcept (id, cc_text) VALUES
                                                 ('slü4',NULL), ('ccpro1','oval'), ('ccpro2','pink'), ('ccpro3','894'),
                                                 ('co1',NULL), ('co2',NULL), ('co3',NULL), ('participant1-type-cc',NULL);

INSERT INTO co_coding (id, co_system, co_code, cc_coding_id) VALUES
                                                                 ('pro098','http://ema.europa.eu/example/manufactureddoseform','Film-coatedtablet','slü4'),
                                                                 ('pro123','shape',NULL,'co1'), ('pro2','color',NULL,'pro1'), ('pro3','imprint',NULL,'plf4'),
                                                                 ('participant1-type-coding','http://terminology.hl7.org/CodeSystem/v3-ParticipationType','PPRF','participant1-type-cc');

INSERT INTO ma_manufactureditemdef (id, ma_status, dr_na_id, manufacturedDoseForm) VALUES
    ('example','active','123','slü4');

INSERT INTO pr_property (id, co_pr_id, valueCodeableConcept, pr_ma_id) VALUES
                                                                           ('example1','co1','ccpro1','example'),
                                                                           ('example2','co2','ccpro2','example'),
                                                                           ('example3','co3','ccpro3','example');

-- ========================
-- 10. References / Encounters
-- ========================

INSERT INTO re_reference (id, re_reference, re_type, re_display, re_pa_id) VALUES
    ('789mno','/1234/Practitioner','Practitioner','Dr. Anna Müller','098');

INSERT INTO e_encounter (id, e_status, r_subject_id) VALUES
    ('home','completed','subject1');

INSERT INTO ep_encounter_participant (id, pe_ep_id, r_ep_id, pe_ep_id) VALUES
                                                                           ('participant1','home','participant1-actor','789xyz'),
                                                                           ('participant2','home','participant2-actor',NULL);

-- Medication Batch
INSERT INTO mb_medication_batch (id, mb_lot_number, mb_expiration_date)
VALUES (1, 'AT 9035 ZU69', '2030-11-30');

-- Form CodeableConcept
INSERT INTO cc_codeableconcept (id, cc_text)
VALUES (1, 'Injection Solution (qualifier value)');

-- Form Coding
INSERT INTO c_coding (id, c_system, c_code, c_display, cc_coding_id)
VALUES (1, 'http://snomed.info/sct', '385219001', 'Injection Solution (qualifier value)', 1);

-- Ingredient 1: Dopamine CodeableConcept
INSERT INTO cc_codeableconcept (id, cc_text)
VALUES (2, 'Dopamine');

INSERT INTO c_coding (id, c_system, c_code, c_display, cc_coding_id)
VALUES (2, 'http://www.nlm.nih.gov/research/umls/rxnorm', '1114879', 'Dopamine', 2);

-- Ingredient 1: Strength Numerator
INSERT INTO q_quantity (id, q_value, q_system, q_code, q_unit)
VALUES (1, 400, 'http://unitsofmeasure.org', 'mg', 'mg');

-- Ingredient 1: Strength Denominator
INSERT INTO q_quantity (id, q_value, q_system, q_code, q_unit)
VALUES (2, 500, 'http://unitsofmeasure.org', 'mL', 'mL');

-- Ingredient 1: Strength Ratio
INSERT INTO r_ratio (id, r_numerator_id, r_denominator_id)
VALUES (1, 1, 2);

-- Ingredient 1
INSERT INTO mi_medication_ingredient (id, mi_item_cc_id, mi_strength_id)
VALUES (1, 2, 1);

-- Ingredient 2: Dextrose CodeableConcept
INSERT INTO cc_codeableconcept (id, cc_text)
VALUES (3, 'Dextrose 5% injection USP');

INSERT INTO c_coding (id, c_system, c_code, c_display, cc_coding_id)
VALUES (3, 'http://hl7.org/fhir/sid/ndc', '0264751010', 'Dextrose 5% injection USP', 3);

-- Ingredient 2: Strength Numerator
INSERT INTO q_quantity (id, q_value, q_system, q_code, q_unit)
VALUES (3, 5, 'http://unitsofmeasure.org', 'g', 'g');

-- Ingredient 2: Strength Denominator
INSERT INTO q_quantity (id, q_value, q_system, q_code, q_unit)
VALUES (4, 100, 'http://unitsofmeasure.org', 'mL', 'mL');

-- Ingredient 2: Strength Ratio
INSERT INTO r_ratio (id, r_numerator_id, r_denominator_id)
VALUES (2, 3, 4);

-- Ingredient 2
INSERT INTO mi_medication_ingredient (id, mi_item_cc_id, mi_strength_id)
VALUES (2, 3, 2);



-- Medication Resource
INSERT INTO m_medication (id, m_status, m_form_id, m_batch_id, dr_na_id)
VALUES ('med0336', 'active', 1, 1, 1);

-- Link Ingredients to Medication
UPDATE mi_medication_ingredient SET mi_m_id = 'med0336' WHERE id IN (1, 2);


INSERT INTO plf1_1_plf (id, top_plf,i_plf_id)
VALUES ('e11', 'searchset', example);

INSERT INTO en_plf(id, f_en, r_en)
VALUES ('e11', "https://e11", 'Medication');

INSERT INTO na_narrative (id, n_status, n_div, ) VALUES
    ('example','generates','<div xe11res</div>');

-- Observation Example: Creatinine (based on FHIR observation-example-f204-creatinine)
--
-- This example demonstrates a lab observation for serum creatinine levels

-- Insert CodeableConcept for observation code (LOINC: Creatinine)
INSERT INTO cc_codeableconcept (id, cc_text) VALUES
    ('cc_obs_code_creat_1', 'Creatinine(Serum)');

-- Insert Coding for the code
INSERT INTO co_coding (id, co_system, co_code, co_display, cc_coding_id) VALUES
    ('coding_loinc_creat_1', 'http://loinc.org', '2160-0', 'Creatinine [Mass/volume] in Serum or Plasma', 'cc_obs_code_creat_1');

-- Insert Reference for subject (Patient)
INSERT INTO re_reference (id, re_reference, re_display) VALUES
    ('ref_patient_f204_1', 'Patient/f201', 'Roel');

-- Insert Reference for performer (Practitioner)
INSERT INTO re_reference (id, re_reference, re_display) VALUES
    ('ref_pract_f204_1', 'Practitioner/f202', 'Luigi Maas');

-- Insert CodeableConcept for interpretation
INSERT INTO cc_codeableconcept (id, cc_text) VALUES
    ('cc_interp_f204_1', 'Serum creatinine raised');

INSERT INTO co_coding (id, co_system, co_code, co_display, cc_coding_id) VALUES
                                                                             ('coding_interp_f204_1', 'http://snomed.info/sct', '166717003', 'Serum creatinine raised', 'cc_interp_f204_1'),
                                                                             ('coding_interp_v3_f204_1', 'http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation', 'H', 'High', 'cc_interp_f204_1');

-- Insert Quantity for value
INSERT INTO q_quantity (id, q_value, q_unit, q_system, q_code, q_comparator) VALUES
    ('quant_creat_f204_1', 122.0, 'umol/L', 'http://unitsofmeasure.org', 'umol/L', NULL);

-- Insert SimpleQuantity for reference range low
INSERT INTO sq_simple_quantity (id, sq_value, sq_unit, sq_system, sq_code) VALUES
    ('sq_low_f204_1', 64.0, 'umol/L', 'http://unitsofmeasure.org', 'umol/L');

-- Insert SimpleQuantity for reference range high
INSERT INTO sq_simple_quantity (id, sq_value, sq_unit, sq_system, sq_code) VALUES
    ('sq_high_f204_1', 104.0, 'umol/L', 'http://unitsofmeasure.org', 'umol/L');

-- Insert ObservationReferenceRange
INSERT INTO orr_observation_reference_range (id, orr_low_id, orr_high_id) VALUES
    ('orr_f204_1', 'sq_low_f204_1', 'sq_high_f204_1');

-- Insert main Observation
INSERT INTO o_observation (id, o_status, o_code_id, o_subject_id, o_effective_datetime,
                           o_issued, o_value_quantity_id) VALUES
    ('obs_f204_creatinine', '_final', 'cc_obs_code_creat_1', 'ref_patient_f204_1',
     '2013-04-02 10:30:10', '2013-04-03 15:30:10', 'quant_creat_f204_1');

-- Link performer
UPDATE re_reference SET i_o_id = 'obs_f204_creatinine' WHERE id = 'ref_pract_f204_1';

-- Link interpretation
UPDATE cc_codeableconcept SET o_interpretation_id = 'obs_f204_creatinine' WHERE id = 'cc_interp_f204_1';

-- Link reference range
UPDATE orr_observation_reference_range SET orr_o_id = 'obs_f204_creatinine' WHERE id = 'orr_f204_1';


-- Additional Example: Blood Pressure Observation with Components

-- Insert CodeableConcept for BP observation code
INSERT INTO cc_codeableconcept (id, cc_text) VALUES
    ('cc_obs_bp_1', 'Blood Pressure');

INSERT INTO co_coding (id, co_system, co_code, co_display, cc_coding_id) VALUES
    ('coding_bp_1', 'http://loinc.org', '85354-9', 'Blood pressure panel', 'cc_obs_bp_1');

-- Insert Reference for BP subject
INSERT INTO re_reference (id, re_reference, re_display) VALUES
    ('ref_patient_bp_1', 'Patient/example', 'Peter Patient');

-- Insert main BP Observation
INSERT INTO o_observation (id, o_status, o_code_id, o_subject_id, o_effective_datetime) VALUES
    ('obs_bp_example', '_final', 'cc_obs_bp_1', 'ref_patient_bp_1', '2024-01-15 09:00:00');

-- Component 1: Systolic BP
INSERT INTO cc_codeableconcept (id, cc_text) VALUES
    ('cc_comp_systolic_1', 'Systolic Blood Pressure');

INSERT INTO co_coding (id, co_system, co_code, co_display, cc_coding_id) VALUES
    ('coding_systolic_1', 'http://loinc.org', '8480-6', 'Systolic blood pressure', 'cc_comp_systolic_1');

INSERT INTO q_quantity (id, q_value, q_unit, q_system, q_code) VALUES
    ('quant_systolic_1', 120.0, 'mm[Hg]', 'http://unitsofmeasure.org', 'mm[Hg]');

INSERT INTO oc_observation_component (id, oc_code_id, oc_value_quantity_id) VALUES
    ('comp_systolic_1', 'cc_comp_systolic_1', 'quant_systolic_1');

-- Component 2: Diastolic BP
INSERT INTO cc_codeableconcept (id, cc_text) VALUES
    ('cc_comp_diastolic_1', 'Diastolic Blood Pressure');

INSERT INTO co_coding (id, co_system, co_code, co_display, cc_coding_id) VALUES
    ('coding_diastolic_1', 'http://loinc.org', '8462-4', 'Diastolic blood pressure', 'cc_comp_diastolic_1');

INSERT INTO q_quantity (id, q_value, q_unit, q_system, q_code) VALUES
    ('quant_diastolic_1', 80.0, 'mm[Hg]', 'http://unitsofmeasure.org', 'mm[Hg]');

INSERT INTO oc_observation_component (id, oc_code_id, oc_value_quantity_id) VALUES
    ('comp_diastolic_1', 'cc_comp_diastolic_1', 'quant_diastolic_1');

-- Link components to observation
UPDATE oc_observation_component SET oc_o_id = 'obs_bp_example' WHERE id IN ('comp_systolic_1', 'comp_diastolic_1');


-- Example 3: Temperature Observation

INSERT INTO cc_codeableconcept (id, cc_text) VALUES
    ('cc_obs_temp_1', 'Body Temperature');

INSERT INTO co_coding (id, co_system, co_code, co_display, cc_coding_id) VALUES
    ('coding_temp_1', 'http://loinc.org', '8310-5', 'Body temperature', 'cc_obs_temp_1');

INSERT INTO re_reference (id, re_reference, re_display) VALUES
    ('ref_patient_temp_1', 'Patient/temp001', 'Anna Schmidt');

INSERT INTO q_quantity (id, q_value, q_unit, q_system, q_code) VALUES
    ('quant_temp_1', 37.2, 'Cel', 'http://unitsofmeasure.org', 'Cel');

INSERT INTO o_observation (id, o_status, o_code_id, o_subject_id, o_effective_datetime,
                           o_value_quantity_id) VALUES
    ('obs_temp_example', '_final', 'cc_obs_temp_1', 'ref_patient_temp_1',
     '2024-02-09 14:30:00', 'quant_temp_1');


-- ===== PLF2_1 "example-saner" Import (laut JSON-Angabe) =====
-- Reihenfolge: Zuerst abhängige Objekte, dann Hauptentität

-- 1. Exn (Extension) erstellen - enthält die Expression Language
INSERT INTO spengerfhirmed.e_exn (id, tl) VALUES ('plf_exn_1', 'text_fhirpath');

-- 2. PLF2_1 Hauptentität erstellen
INSERT INTO spengerfhirmed.plf_plf21 (id, plf_st) VALUES ('example-saner', 'active');

-- 3. R (Access Regards/Regel) erstellen mit FK zu PLF2_1
INSERT INTO spengerfhirmed.r_r (id, r_type, r_plf_id) VALUES ('plf_r_1', 'permit', 'example-saner');

-- 4. Data erstellen mit FK zu Exn und R
INSERT INTO spengerfhirmed.d_data (id, e_d_id, d_r_id) VALUES ('plf_data_1', 'plf_exn_1', 'plf_r_1');

-- 5. Optional: Datum für PLF2_1 (ElementCollection)
INSERT INTO spengerfhirmed.plf_date (plf_id, date) VALUES ('example-saner', '2025-02-25 00:00:00');