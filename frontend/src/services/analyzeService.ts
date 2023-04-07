import { map } from "../mappers/analyzeMapper";
import { Method } from "../model/model";
import data from "../sourcePrinterOut.json";

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
  // First, sort Methods by number of calls, so top 5 is first in list.
  // Then, filter out lambda/internal functions.
  const arr = fetchAnalyzedInner().methods
              .sort((a, b) => a.calls > b.calls ? -1 : a.calls < b.calls ? 1 : 0)
              .filter(m => !m.name?.includes("$"))
              .filter(m => m.source !== "")
              
  if (arr.length > 5) {
    for (let i = 5; i < arr.length; i++) {
      delete arr[i]
    }
  }
  arr.forEach((m) => console.log(m.source.length))
  return arr.map((m) => map(m));
};
