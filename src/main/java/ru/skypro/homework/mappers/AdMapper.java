package ru.skypro.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.entity.Ad;

@Mapper(uses = {UserMapper.class}, componentModel = "spring")
public interface AdMapper {
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "imageUrl", source = "imageUrl")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "title", source = "title")
    AdDto adToAdDto(Ad ad);

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Ad adDtoToAd(AdDto adDto);
}
