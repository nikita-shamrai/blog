package ua.shamray.myblogspringbootv1.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.shamray.myblogspringbootv1.dto.AccountDTO;
import ua.shamray.myblogspringbootv1.dto.AccountViewer;
import ua.shamray.myblogspringbootv1.model.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(target = "password", ignore = true)
    AccountDTO entityToDTO(Account account);
    @Mapping(target = "id", ignore = true)
    Account dtoToEntity(AccountDTO accountDTO);

    AccountViewer entityToViewer(Account account);

}
