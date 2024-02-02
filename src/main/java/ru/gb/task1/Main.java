package ru.gb.task1;

import ru.gb.models.Course;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class Main {
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Course.class)
                .buildSessionFactory()){

            //Создание сессии
            Session session = sessionFactory.getCurrentSession();

            //Создание транзакции
            session.beginTransaction();

            //Создание объекта
            Course course = Course.createCourses();

            //Добавление объекта
            session.save(course);
            System.out.println("Object is successfully saved!");

            //Чтение объекта
            Course readCourse = session.get(Course.class, course.getId());
            System.out.println("Course: " + readCourse);
            System.out.println("Reading is successfully done!");

            //Обновление объекта
            session.update(readCourse);
            System.out.println("Course is successfully updated!");

            //Удаление объекта
            session.delete(readCourse);
            System.out.println("Course is successfully deleted!");

            session.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
