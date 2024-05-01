INSERT INTO patients (patientId,birthDate,firstName,lastName,pesel) VALUES (1, '1995-12-10', 'Jan', 'Kowalski', '123456789');
INSERT INTO patients (patientId,birthDate,firstName,lastName,pesel) VALUES (2, '1992-11-11', 'Michał', 'Brzęczyszczykiewicz', '987654321');
INSERT INTO doctors (doctorId, birthDate, firstName, lastName, pesel, nip, specialty) VALUES (1, '1956-02-02', 'Joanna', 'Michalska',  '987123478', '12345257792', 'neurochirug');
INSERT INTO doctors (doctorId, birthDate, firstName, lastName, pesel, nip, specialty) VALUES (2, '1988-03-03', 'Patryk', 'Nowicki',  '1235743242', '1236865332', 'pediatra');
INSERT INTO visits (doctor_Id, patient_Id, visitDate) VALUES (1, 1, '2024-05-02');
INSERT INTO visits (doctor_Id, patient_Id, visitDate) VALUES (2, 2, '2024-05-03');
