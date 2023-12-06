package com.project.kpaas.popup.repository;


import com.project.kpaas.popup.dto.PopupResponseDto;
import com.project.kpaas.popup.entity.Popupstore;
import com.project.kpaas.popup.entity.QPopupstore;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.locationtech.jts.geom.Point;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
//import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.project.kpaas.popup.entity.QPopupstore.popupstore;

@Repository
public class PopupRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;
    private final QPopupstore qPopupstore = popupstore;

    public List<Popupstore> findAllByGps(double lat, double lon, double rad) {
        JPAQuery<Popupstore> result = new JPAQuery<>(entityManager);

        double centerX = lat;
        double centerY = lon;
        double radius = rad;

        Point centerPoint = new GeometryFactory().createPoint(new Coordinate(lon, lat));

        return new ArrayList<>(result.select(qPopupstore)
                .from(qPopupstore)
                .where(
                        Expressions.numberTemplate(Double.class, "ST_Distance_Sphere({0}, POINT({1}, {2}))", qPopupstore.gps, centerX, centerY)
                                .loe(radius)
                )
//                .where(qPopupstore.gps.distance(centerPoint).loe(radius))
                .distinct()
                .fetch());
    }

    public List<Popupstore> getPopupsByRadius(double lat, double lon, double radius) {
//        String queryString = "SELECT p.id, p.popupName, p.gps " +
        String queryString = "SELECT p " +
                "FROM Popupstore p " +
                "WHERE ST_Distance_Sphere(p.gps, POINT(:centerX, :centerY)) <= :radius";

        Query query = entityManager.createQuery(queryString);

        // SQL문 파라미터 설정
        query.setParameter("centerX", lat);
        query.setParameter("centerY", lon);
        query.setParameter("radius", radius);

        return query.getResultList();
    }
}




