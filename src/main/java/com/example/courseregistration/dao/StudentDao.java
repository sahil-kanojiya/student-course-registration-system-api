package com.example.courseregistration.dao;

import com.example.courseregistration.entity.Student;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class StudentDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Student save(Student student) {
        entityManager.persist(student);
        return student;
    }

    @Transactional
    public Student update(Student student) {
        return entityManager.merge(student);
    }

    @Transactional
    public void delete(Long studentId) {
        Student student = entityManager.find(Student.class, studentId);
        if (student != null) {
            entityManager.remove(student);
        }
    }

    @Transactional(readOnly = true)
    public Student findById(Long studentId) {
        return entityManager.find(Student.class, studentId);
    }

    @Transactional(readOnly = true)
    public Optional<Student> findByEmail(String email) {
        List<Student> resultList = entityManager
                .createQuery("SELECT s FROM Student s WHERE s.email = ?1", Student.class)
                .setParameter(1, email)
                .getResultList();
        return resultList.stream().findFirst();
    }

    @Transactional(readOnly = true)
    public List<Student> findAll() {
        return entityManager.createQuery("SELECT s FROM Student s", Student.class).getResultList();
    }

}
