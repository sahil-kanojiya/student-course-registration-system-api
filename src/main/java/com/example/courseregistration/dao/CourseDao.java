package com.example.courseregistration.dao;

import com.example.courseregistration.entity.Course;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CourseDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Course save(Course course) {
        entityManager.persist(course);
        return course;
    }

    @Transactional
    public Course update(Course course) {
        return entityManager.merge(course);
    }

    @Transactional
    public void delete(Long courseId) {
        Course course = entityManager.find(Course.class, courseId);
        if (course != null) {
            entityManager.remove(course);
        }
    }

    @Transactional(readOnly = true)
    public Course findById(Long courseId) {
        return entityManager.find(Course.class, courseId);
    }

    @Transactional(readOnly = true)
    public List<Course> findAll() {
        return entityManager.createQuery("SELECT c FROM Course c", Course.class).getResultList();
    }

}
