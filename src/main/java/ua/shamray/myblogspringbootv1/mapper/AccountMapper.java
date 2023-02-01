package ua.shamray.myblogspringbootv1.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.shamray.myblogspringbootv1.dto.AccountDTO;
import ua.shamray.myblogspringbootv1.model.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDTO entityToDTO(Account account);

    Account dtoToEntity(AccountDTO accountDTO);

}
