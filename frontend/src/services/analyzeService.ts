import { map } from "../mappers/analyzeMapper";
import { Method } from "../model/model";
import data from "../sourcePrinterOutput.json";

export interface MethodDto {
  name: string;
  calls: number;
  source: string;
}

export interface AnalyzeReponse {
  methods: MethodDto[];
}

const fetchAnalyzedInner = (): AnalyzeReponse => {
  const response: AnalyzeReponse = data;

  return response;
};

export const fetchAnalyzed = (): Method[] => {
  return fetchAnalyzedInner().methods.map((m) => map(m));
};
