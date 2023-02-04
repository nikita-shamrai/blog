package ua.shamray.myblogspringbootv1.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import ua.shamray.myblogspringbootv1.dto.PostDTO;
import ua.shamray.myblogspringbootv1.model.Post;
import ua.shamray.myblogspringbootv1.service.AccountService;


@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {AccountMapper.class})
public abstract class PostMapper {
    @Autowired
    protected AccountMapper accountMapper;
    @Autowired
    protected AccountService accountService;

    @Mapping(target = "accountViewer", expression = "java(accountMapper.entityToViewer(post.getAccount()))")
    public abstract PostDTO entityToDTO(Post post);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account", ignore = true)
    public abstract Post dtoToEntity(PostDTO postDTO);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    public abstract Post mapEntityWithDTO(@MappingTarget Post post, PostDTO postDTO);

}
