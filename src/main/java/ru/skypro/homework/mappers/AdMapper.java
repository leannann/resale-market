package ru.skypro.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.entity.Ad;

@Mapper(uses = {UserMapper.class}, componentModel = "spring")
public interface AdMapper {
    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "image", source = "imageUrl")
    @Mapping(target = "pk", source = "id")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "title", source = "title")
    AdDto adToAdDto(Ad ad);

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "imageUrl", source = "image")
    @Mapping(target = "id", source = "pk")
    Ad adDtoToAd(AdDto adDto);
}
