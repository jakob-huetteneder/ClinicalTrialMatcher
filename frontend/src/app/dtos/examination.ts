
export interface Examination {
  id?: number;
  patientId: number;
  diseaseId: number;
  name: string;
  date: Date;
  type?: string;
  note?: string;
}
