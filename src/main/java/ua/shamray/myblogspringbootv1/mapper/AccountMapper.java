package ua.shamray.myblogspringbootv1.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.shamray.myblogspringbootv1.dto.AccountDTO;
import ua.shamray.myblogspringbootv1.dto.AccountViewer;
import ua.shamray.myblogspringbootv1.entity.Account;
import java.util.ArrayList;
import java.util.HashSet;

@Mapper(componentModel = "spring", imports = {HashSet.class, ArrayList.class})
public interface AccountMapper {
    @Mapping(target = "password", ignore = true)
    AccountDTO entityToDTO(Account account);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", expression = "java(new HashSet<>())")
    @Mapping(target = "postList", expression = "java(new ArrayList<>())")
    Account dtoToEntity(AccountDTO accountDTO);

    AccountViewer entityToViewer(Account account);

}
