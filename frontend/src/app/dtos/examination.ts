
export interface Examination {
  id?: number;
  patientId: number;
  diseaseId: number;
  name: string;
  date: Date;
  type?: string;
  note?: string;
}

export interface Image {
  file: ArrayBuffer;
}
