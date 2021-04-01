package bsep.admin.mappers;

public interface MapperInterface<E, D> {

        E toEntity(D dto);

        D toDto(E entity);
}
