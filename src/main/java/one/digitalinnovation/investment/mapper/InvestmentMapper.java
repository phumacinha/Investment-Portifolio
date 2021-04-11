package one.digitalinnovation.investment.mapper;

import one.digitalinnovation.investment.dto.InvestmentDTO;
import one.digitalinnovation.investment.entity.Investment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InvestmentMapper {
    InvestmentMapper INSTANCE = Mappers.getMapper(InvestmentMapper.class);

    Investment toModel(InvestmentDTO investmentDTO);

    InvestmentDTO toDTO(Investment investment);
}
