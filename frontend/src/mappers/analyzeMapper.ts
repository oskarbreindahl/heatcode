import { Method } from "../model/model"
import { MethodDto } from "../services/analyzeService"

export const map = (dto: MethodDto): Method => {
    const method: Method = {
        calls: dto.calls,
        source: dto.source,
        referencename: extractReference(dto.name),
        package: extractPackage(dto.name)
    }

    return method;
}

const extractPackage = (fullyQualifiedPath: string): string => {
    const lastDotIndex = fullyQualifiedPath.lastIndexOf('.');
    return fullyQualifiedPath.substring(0, lastDotIndex);
}

const extractReference = (fullyQualifiedPath: string): string => {
    const lastDotIndex = fullyQualifiedPath.lastIndexOf('.');
    return fullyQualifiedPath.substring(lastDotIndex + 1);
}