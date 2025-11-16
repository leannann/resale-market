package ru.skypro.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.entity.Ad;

/**
 * Маппер для преобразования между сущностью {@link Ad} и DTO {@link AdDto}.
 * <p>
 * Использует библиотеку MapStruct для автоматического создания реализации.
 * Выполняет преобразование полей объявления, включая сопоставление
 * идентификатора автора и преобразование пути изображения.
 */
@Mapper(uses = {UserMapper.class}, componentModel = "spring")
public interface AdMapper {

    /**
     * Преобразует сущность {@link Ad} в DTO {@link AdDto}.
     * <p>
     * Особенности маппинга:
     * <ul>
     *     <li>{@code author} — преобразуется в ID автора;</li>
     *     <li>{@code imageUrl → image} — путь изображения переносится в DTO;</li>
     *     <li>{@code id → pk} — ID объявления маппится в поле pk;</li>
     *     <li>остальные поля копируются напрямую.</li>
     * </ul>
     *
     * @param ad сущность объявления
     * @return объект {@link AdDto}
     */
    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "image", source = "imageUrl")
    @Mapping(target = "pk", source = "id")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "title", source = "title")
    AdDto adToAdDto(Ad ad);

    /**
     * Преобразует DTO {@link AdDto} обратно в сущность {@link Ad}.
     * <p>
     * Особенности маппинга:
     * <ul>
     *     <li>{@code author} — игнорируется, т.к. требуется загружать автора отдельно;</li>
     *     <li>{@code description} — игнорируется, т.к. отсутствует в AdDto;</li>
     *     <li>{@code comments} — игнорируются;</li>
     *     <li>{@code image → imageUrl} — путь изображения переносится в сущность;</li>
     *     <li>{@code pk → id} — идентификатор объявления восстанавливается.</li>
     * </ul>
     *
     * @param adDto DTO объявления
     * @return сущность {@link Ad}
     */
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "imageUrl", source = "image")
    @Mapping(target = "id", source = "pk")
    Ad adDtoToAd(AdDto adDto);
}
