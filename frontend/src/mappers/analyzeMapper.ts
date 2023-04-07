import { Method } from "../model/model"
import { MethodDto } from "../services/analyzeService"

export const map = (dto: MethodDto): Method => {
    const method: Method = {
        calls: dto.calls,
        source: dto.source,
        referencename: dto.name,
        package: dto.package
    }

    return method;
}