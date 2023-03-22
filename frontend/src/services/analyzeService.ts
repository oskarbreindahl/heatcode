import { map } from "../mappers/analyzeMapper";
import { Method } from "../model/model";

export interface MethodDto {
    name: string;
    calls: number;
    source: string;
}

export interface AnalyzeReponse {
    methods: MethodDto[];
}

const fetchAnalyzedInner = (): AnalyzeReponse => {
    const response: AnalyzeReponse = {
        methods: [
            {
                name: "com.whatever.silk.main",
                calls: 1,
                source: "public static void main(String[] args){System.out.println(\"hej\");}"
            },
            {
                name: "com.whatever.silk.helpme",
                calls: 50,
                source: "public static void helpme(){int x = 1;}"
            }
        ]
    }

    return response;
}

export const fetchAnalyzed = (): Method[] => {
    return fetchAnalyzedInner().methods.map(m => map(m));
}