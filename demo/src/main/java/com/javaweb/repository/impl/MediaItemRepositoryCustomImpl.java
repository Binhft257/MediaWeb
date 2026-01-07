package com.javaweb.repository.impl;

import com.javaweb.entity.MediaItemEntity;
import com.javaweb.model.request.MediaSearchRequest;
import com.javaweb.repository.MediaItemRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class MediaItemRepositoryCustomImpl implements MediaItemRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private void join(MediaSearchRequest request, StringBuilder sql) {

        if (request.getTypeName() != null && !request.getTypeName().equals("")) {
            sql.append(" LEFT JOIN media_type mt ON mt.media_type_id = mi.media_type_id ");
        }

        if (request.getGenre() != null && !request.getGenre().equals("")) {
            sql.append(" LEFT JOIN media_genre mg ON mg.media_item_id = mi.media_item_id ");
            sql.append(" LEFT JOIN genre g ON g.genre_id = mg.genre_id ");
        }
    }

    private void whereClause(MediaSearchRequest request, StringBuilder where) {

        if (request.getTitle() != null && !request.getTitle().equals("")) {
            where.append(" AND mi.title LIKE '%").append(request.getTitle()).append("%' ");
        }

        if (request.getCountry() != null && !request.getCountry().equals("")) {
            where.append(" AND mi.country LIKE '%").append(request.getCountry()).append("%' ");
        }

        if (request.getTypeName() != null && !request.getTypeName().equals("")) {
            where.append(" AND mt.type_name LIKE '%").append(request.getTypeName()).append("%' ");
        }

        if (request.getGenre() != null && !request.getGenre().equals("")) {
            where.append(" AND g.genre_name LIKE '%").append(request.getGenre()).append("%' ");
        }
    }

    @Override
    public List<MediaItemEntity> getMediasWithCondition(Pageable pageable, MediaSearchRequest request) {

        StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT mi.* FROM media_item mi "
        );

        join(request, sql);

        StringBuilder where = new StringBuilder(" WHERE 1=1 ");
        whereClause(request, where);

        sql.append(where);

        if(pageable != null) {
            sql.append(" LIMIT ")
                    .append(pageable.getPageSize())
                    .append(" OFFSET ")
                    .append(pageable.getOffset());
        }
        Query query = entityManager.createNativeQuery(sql.toString(), MediaItemEntity.class);
        return query.getResultList();
    }
}
