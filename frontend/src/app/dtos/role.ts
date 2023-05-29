export enum Role {
  patient = 'PATIENT',
  doctor = 'DOCTOR',
  researcher = 'RESEARCHER',
  admin = 'ADMIN'
}

export const getRoleString = (role: Role): string => {
  switch (role) {
    case Role.patient:
      return 'Patient';
    case Role.doctor:
      return 'Doctor';
    case Role.researcher:
      return 'Researcher';
    case Role.admin:
      return 'Admin';
    default:
      return 'Unknown';
  }
};
