package ua.shamray.myblogspringbootv1.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import ua.shamray.myblogspringbootv1.dto.PostDTO;
import ua.shamray.myblogspringbootv1.model.Post;
import ua.shamray.myblogspringbootv1.service.AccountService;

import java.util.NoSuchElementException;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {AccountMapper.class})
public abstract class PostMapper {
    @Autowired
    protected AccountMapper accountMapper;
    @Autowired
    protected AccountService accountService;

    @Mapping(target = "accountDTO", expression = "java(accountMapper.entityToDTO(post.getAccount()))")
    public abstract PostDTO entityToDTO(Post post);

    @Mapping(target = "account",
             expression = "java(accountService.getById(postDTO.getAccountDTO().getId())" +
                        ".orElseThrow(() ->  new IllegalArgumentException(\"Account with id=\" + postDTO.getAccountDTO().getId() + \" not found.\")))")
    @Mapping(target = "id", ignore = true)
    public abstract Post dtoToEntity(PostDTO postDTO);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    public abstract Post mapEntityWithDTO(@MappingTarget Post post, PostDTO postDTO);

}
